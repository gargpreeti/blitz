package com.zoptal.blitz.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Business_SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    final String REGISTER_URL = RegisterUrl.signup;

    private TextView tv_signin;
    private RelativeLayout btn_signup;
    private EditText ed_name,ed_email,ed_password,ed_code,ed_confirmpassword;

    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__signup);

//        gps = new GPSTracker(Business_SignUpActivity.this);
        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        initview();
    }


    public void initview(){

        tv_signin=(TextView)findViewById(R.id.tv_signin);
        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_password=(EditText)findViewById(R.id.ed_password);
        ed_code=(EditText)findViewById(R.id.ed_code);
        ed_confirmpassword=(EditText)findViewById(R.id.ed_confirmpassword);
        btn_signup=(RelativeLayout)findViewById(R.id.btn_signup);



        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Business_SignUpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Business_SignUpActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                Log.e("lat---",""+latitude);
                Log.e("long---",""+longitude);

                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }

        btn_signup.setOnClickListener(this);
        tv_signin.setOnClickListener(this);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, Business_SignUpActivity.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_signin:
                Intent i = new Intent(Business_SignUpActivity.this, Business_SigninActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_signup:

                Log.e("ed textttt",""+ed_code.getText().toString().trim());

                if(ed_code.getText().toString().trim().isEmpty()) {

                    dialogalert();
                }
                else{

                    if (NetworkConnection.isConnectedToInternet(Business_SignUpActivity.this)) {
                        registerUser();

                    }
                    else {
                        Toast.makeText(Business_SignUpActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }


                break;

        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.e("mainactivity1----","restart");
        finish();
        startActivity(getIntent());
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private void registerUser(){

        final String username = ed_name.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();
        final String cpw= ed_confirmpassword.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();
        final String usercode = ed_code.getText().toString().trim();


        if(username.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_SignUpActivity.this, "Please enter your name.");

            alertmsg.dialog.show();

            return;
        }
        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_SignUpActivity.this, "Please enter your email.");

            alertmsg.dialog.show();
            return;
        }

        if(password.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_SignUpActivity.this, "Please enter your password.");

            alertmsg.dialog.show();
            return;
        }

        if(cpw.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_SignUpActivity.this, "Confirm password must contain minimum 8 characters.");

            alertmsg.dialog.show();
            return;
        }

//        if(username.equals("")||password.equals("")||cpw.equals("")||email.equals("")){
//
//                Toast.makeText(Business_SignUpActivity.this,"All fields are compulsory!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//

        if (!isValidEmail(email)) {
            ed_email.setError("Invalid Email");
            return;
        }

        if (!isValidPassword(password)) {


            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_SignUpActivity.this, "Password must contain minimum 8 characters.");

            alertmsg.dialog.show();


          //  Toast.makeText(Business_SignUpActivity.this,"Password must contain minimum 8 characters.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!cpw.equals(password)){
            ed_confirmpassword.setError("Password and confirm password should be same");
            return;
        }

        if( String.valueOf(latitude).equals("0.0") || String.valueOf(longitude).equals("0.0")){
//
//         AlertDialogMsg alertmsg = new
//                 AlertDialogMsg(Buyer_SignUpActivity.this,"Your location must be on");
//
//         alertmsg.dialog.show();
            Toast.makeText(Business_SignUpActivity.this,"Your location must be on in app.", Toast.LENGTH_SHORT).show();

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Business_SignUpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
                gps = new GPSTracker(mContext, Business_SignUpActivity.this);

                // Check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    Log.e("lat---",""+latitude);
                    Log.e("long---",""+longitude);

                    // \n is for new line
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
            }

            return;
        }
            loading = new ProgressDialog(Business_SignUpActivity.this,R.style.AppCompatAlertDialogStyle);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();



                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg =main_obj.getString("message");

                            Toast.makeText(Business_SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();

                            JSONObject  obj=main_obj.getJSONObject("data");

                            String id =obj.getString("id");
                            String access_token =obj.getString("access_token");

                            SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                            editor1.putString("id",id);
                            editor1.putString("access_token",access_token);
                            editor1.commit();

                            Intent intentwillow= new Intent(Business_SignUpActivity.this, AndroidImageSlider_Signup.class);
                            startActivity(intentwillow);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_SignUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",username);
                params.put("email",email);
                params.put("password", password);
                params.put("confirm_password", cpw);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("device_type", "android");
                params.put("device_token", "12345678");
                params.put("user_type", "1");
                params.put("promo_code", usercode);
                params.put("firebase_registration_id",MainActivity.regId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // validating password with retype password
    private boolean isValidPassword(String password) {
        if (password != null && password.length() > 7) {
            return true;
        }
        return false;
    }


    public void dialogalert() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_SignUpActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText("Are you sure you don't have a Promo code");
        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.isConnectedToInternet(Business_SignUpActivity.this)) {
                    registerUser();

                }
                else {
                    Toast.makeText(Business_SignUpActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
