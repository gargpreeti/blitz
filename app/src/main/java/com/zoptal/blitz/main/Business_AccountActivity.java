package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Business_AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow,user_img,img_cross;
    private EditText ed_name,ed_email,ed_phone;
    private Button btn_clear,btn_save;
    private Bitmap bitmap;

    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    String name,email,phone,profile_pic;
 String userimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__account);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        initview();
    }


    public void initview(){

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        user_img=(ImageView)findViewById(R.id.user_img);
        img_cross=(ImageView)findViewById(R.id.img_cross);
        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_phone=(EditText)findViewById(R.id.ed_phone);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        btn_save=(Button)findViewById(R.id.btn_save);


        img_backarrow.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        user_img.setOnClickListener(this);

        Intent intent = getIntent();
        name=intent.getStringExtra("name");
        email=intent.getStringExtra("email");
        phone=intent.getStringExtra("phone");
        profile_pic=intent.getStringExtra("profile_pic");

        ed_name.setText(name);
        ed_email.setText(email);
        ed_phone.setText(phone);

      // bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.user);

        try {
            if (profile_pic.isEmpty()) {

                Log.e("empyyy","test");


            }
            else {
Log.e("profile pic---",""+profile_pic);
                Picasso.with(Business_AccountActivity.this).load(profile_pic).into(user_img);
            }
        }
        catch (NullPointerException e){

        }

      // bitmap = ((BitmapDrawable)user_img.getDrawable()).getBitmap();

        btn_save.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        img_cross.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_AccountActivity.this, Business_AccountsettingActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btn_save:

                if (NetworkConnection.isConnectedToInternet(Business_AccountActivity.this)) {

                    userprofileupdate();

                }
                else {
                    Toast.makeText(Business_AccountActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }


                break;

            case R.id.btn_clear:

                ed_name.setText("");
                ed_email.setText("");
                ed_phone.setText("");

                break;

            case R.id.user_img:

                Crop.pickImage(Business_AccountActivity.this, 1);

                break;

            case R.id.img_cross:

                Intent intentcross = new Intent(Business_AccountActivity.this, Business_AccountsettingActivity.class);
                startActivity(intentcross);
                finish();
                break;
        }
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_AccountActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_AccountActivity.this, 1);

    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            user_img.setImageURI(null);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            user_img.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_AccountActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            onActivityResult1(requestCode, resultCode, data);
        }

    }
    public void onActivityResult1(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == 1) {
                beginCrop(data.getData());

            }
            if (requestCode == 1) {

                handleCrop(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void userprofileupdate(){

        final String username = ed_name.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();
        final String phone= ed_phone.getText().toString().trim();


        if(username.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AccountActivity.this, "Please enter the name first.");

            alertmsg.dialog.show();
            return;
        }



        if(email.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AccountActivity.this, "Please enter email address.");

            alertmsg.dialog.show();
            return;
        }




        if(phone.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AccountActivity.this, "Please enter phone number.");

            alertmsg.dialog.show();
            return;
        }
        Log.e("bitmap value=====",""+bitmap);
        try {
            if (bitmap==null) {

//               bitmap =((BitmapDrawable)user_img.getDrawable()).getBitmap();
//
//                Log.e("bitmap null=====",""+bitmap);
//                userimg = getStringImage(bitmap);
//
//                AlertDialogMsg alertmsg = new
//                        AlertDialogMsg(Business_AccountActivity.this, "Please select image.");
//
//                alertmsg.dialog.show();

                BitmapDrawable drawable = (BitmapDrawable) user_img.getDrawable();
                bitmap = drawable.getBitmap();
                userimg=getStringImage(bitmap);
                return;
            }
        }
            catch (NullPointerException e){

            }
        try {
            if (bitmap != null) {
                Log.e("bitmap not null=====",""+bitmap);
                userimg = getStringImage(bitmap);

            }
        }
        catch (NullPointerException e){

        }



        loading = new ProgressDialog(Business_AccountActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.update_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        //  Toast.makeText(Business_ChangePwActivity.this,"User Registration Successfully",Toast.LENGTH_LONG).show();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code =main_obj.getString("code");
                            if(code.equals("201")){
                                String message =main_obj.getString("message");
                                Toast.makeText(Business_AccountActivity.this,message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                String message =main_obj.getString("message");
                                Toast.makeText(Business_AccountActivity.this,message,Toast.LENGTH_LONG).show();

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
                          //  Toast.makeText(Business_AccountActivity.this, error.toString(), Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",username);
                params.put("email",email);
                params.put("phone_number", phone);
                params.put("profile_pic", userimg);
                params.put("user_type", "1");
                params.put("access_token",access_tokn);

                Log.e("account value----",""+params);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
