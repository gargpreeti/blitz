package com.zoptal.blitz.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class Buyer_SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_signup,tv_signuppost;
    private EditText ed_email,ed_password;
    private ImageView img_keepmesigned,img_keepmesignedselectd;
    private Button btn_forgotpw;
    RelativeLayout btn_signin;

    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    private boolean keepvalue=false;
    String buyerusrname,buyeruserpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_signin);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyerusrname = sharedpreferences1.getString("buyerusrname", "");
        buyeruserpw = sharedpreferences1.getString("buyeruserpw", "");
        initview();
    }


    public void initview(){

        tv_signup=(TextView)findViewById(R.id.tv_signup);
        tv_signuppost=(TextView)findViewById(R.id.tv_signuppost);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_password=(EditText)findViewById(R.id.ed_password);
        img_keepmesigned=(ImageView)findViewById(R.id.img_keepmesigned);
        img_keepmesignedselectd=(ImageView)findViewById(R.id.img_keepmesignedselectd);
        btn_signin=(RelativeLayout)findViewById(R.id.btn_signin);
        btn_forgotpw=(Button)findViewById(R.id.btn_forgotpw);



        ed_email.setText(buyerusrname);
        ed_password.setText(buyeruserpw);



        btn_signin.setOnClickListener(this);
        btn_forgotpw.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_signuppost.setOnClickListener(this);
        img_keepmesigned.setOnClickListener(this);
        img_keepmesignedselectd.setOnClickListener(this);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/timeburnernormal.ttf");
        tv_signup.setTypeface(font);


        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Buyer_SigninActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Buyer_SigninActivity.this);

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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_signup:


                Intent i = new Intent(Buyer_SigninActivity.this,Buyer_SignUpActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.btn_signin:

                if (NetworkConnection.isConnectedToInternet(Buyer_SigninActivity.this)) {

                    userLogin();

                }
                else {
                    Toast.makeText(Buyer_SigninActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }


                break;

            case R.id.btn_forgotpw:

//                Intent intent = new Intent(Buyer_SigninActivity.this, Business_ForgotActivity.class);
//                startActivity(intent);
//                finish();

                break;

            case R.id.tv_signuppost:

                Intent intent_signuppost = new Intent(Buyer_SigninActivity.this,MainActivity.class);
                startActivity(intent_signuppost);
                finish();
                break;

            case R.id.img_keepmesigned:
                keepvalue=true;
                img_keepmesignedselectd.setVisibility(View.VISIBLE);
                img_keepmesigned.setVisibility(View.GONE);

                break;


            case R.id.img_keepmesignedselectd:

                img_keepmesigned.setVisibility(View.VISIBLE);
                img_keepmesignedselectd.setVisibility(View.GONE);

                break;
        }
    }

    private void userLogin() {

        final  String  email = ed_email.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();


        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Buyer_SigninActivity.this, "Please enter your email.");

            alertmsg.dialog.show();
            return;
        }

        if(password.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Buyer_SigninActivity.this, "Please enter your password.");

            alertmsg.dialog.show();
            return;
        }



        if (!isValidEmail(email)) {
            ed_email.setError("Invalid Email");
            return;
        }
        if( String.valueOf(latitude).equals("0.0") || String.valueOf(longitude).equals("0.0")){
//
//         AlertDialogMsg alertmsg = new
//                 AlertDialogMsg(Buyer_SignUpActivity.this,"Your location must be on");
//
//         alertmsg.dialog.show();
            Toast.makeText(Buyer_SigninActivity.this,"Your location must be on in app.", Toast.LENGTH_SHORT).show();

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Buyer_SigninActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
                gps = new GPSTracker(mContext, Buyer_SigninActivity.this);

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
        loading = new ProgressDialog(Buyer_SigninActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.signin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);
                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code = main_obj.getString("code");

                            if (code.equals("201")) {
                                String message = main_obj.getString("message");
                                Toast.makeText(Buyer_SigninActivity.this, message, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // Toast.makeText(Business_SigninActivity.this,message,Toast.LENGTH_SHORT).show();
                            else {
                                JSONObject obj = main_obj.getJSONObject("data");

                                String buyer_id =obj.getString("id");
                                String buyer_access_token =obj.getString("access_token");
                                String buyer_name =obj.getString("name");
                                String buyer_promo_code =obj.getString("promo_code");
                                String buyer_profile_pic =obj.getString("profile_pic");


                                if(keepvalue==false){

                                    SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                    editor1.putString("buyer_id",buyer_id);
                                    editor1.putString("buyer_access_token",buyer_access_token);
                                    editor1.putString("buyer_promo_code",buyer_promo_code);
                                    editor1.putString("buyer_name",buyer_name);
                                    editor1.putString("buyer_profile_pic",buyer_profile_pic);
                                    editor1.putString("buyerusrname","");
                                    editor1.putString("buyeruserpw", "");
                                    editor1.commit();
                                }

                                if(keepvalue==true) {

                                    buyerusrname=email;
                                    buyeruserpw=password;


                                    SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                    editor1.putString("buyer_id",buyer_id);
                                    editor1.putString("buyer_access_token",buyer_access_token);
                                    editor1.putString("buyer_promo_code",buyer_promo_code);
                                    editor1.putString("buyer_name",buyer_name);
                                    editor1.putString("buyer_profile_pic",buyer_profile_pic);
                                    editor1.putString("buyerusrname",buyerusrname);
                                    editor1.putString("buyeruserpw", buyeruserpw);
                                    editor1.commit();
                                }

                       Intent i = new Intent(Buyer_SigninActivity.this, MainActivity1.class);
                                startActivity(i);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Buyer_SigninActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("email",email);
                map.put("password",password);
                map.put("device_type","android");
                map.put("device_token","12345678");
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                map.put("user_type","0");
                map.put("firebase_registration_id",MainActivity.regId);

                return map;
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
}
