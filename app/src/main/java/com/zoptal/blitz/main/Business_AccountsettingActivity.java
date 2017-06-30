package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Business_AccountsettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow, user_img;
    private TextView tv_username;
    private RelativeLayout relative_account, relative_changepw, relative_businesssetting, relative_storesetting,relative_howtopost;
    Bitmap bitmap;

    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn,name,email,phone,profile_pic;

    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__accountsetting);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }


    public void initview() {

        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
        user_img = (ImageView) findViewById(R.id.user_img);
        tv_username=(TextView)findViewById(R.id.tv_username);
        relative_account = (RelativeLayout) findViewById(R.id.relative_account);
        relative_changepw = (RelativeLayout) findViewById(R.id.relative_changepw);
        relative_businesssetting = (RelativeLayout) findViewById(R.id.relative_businesssetting);
        relative_storesetting = (RelativeLayout) findViewById(R.id.relative_storesetting);
        relative_howtopost= (RelativeLayout) findViewById(R.id.relative_howtopost);


        img_backarrow.setOnClickListener(this);
        relative_account.setOnClickListener(this);
        relative_changepw.setOnClickListener(this);
        relative_businesssetting.setOnClickListener(this);
        relative_storesetting.setOnClickListener(this);
        relative_howtopost.setOnClickListener(this);
        user_img.setOnClickListener(this);

        userprofile();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_AccountsettingActivity.this, Business_WillowClothingActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.relative_account:

                Intent intentaccount = new Intent(Business_AccountsettingActivity.this, Business_AccountActivity.class);
                intentaccount.putExtra("name",name);
                intentaccount.putExtra("email",email);
                intentaccount.putExtra("phone",phone);
                intentaccount.putExtra("profile_pic",profile_pic);
                startActivity(intentaccount);

                break;

            case R.id.relative_changepw:

                Intent intentchngepw = new Intent(Business_AccountsettingActivity.this, Business_ChangePwActivity.class);
                startActivity(intentchngepw);

                break;

            case R.id.relative_businesssetting:

                Intent intentbusiness = new Intent(Business_AccountsettingActivity.this, Business_businesssettingActivity.class);
                startActivity(intentbusiness);

                break;


            case R.id.relative_storesetting:

                Intent intentstore = new Intent(Business_AccountsettingActivity.this, Business_businessstoreActivity.class);
                startActivity(intentstore);

                break;


            case R.id.relative_howtopost:

               Intent intentpost= new Intent(Business_AccountsettingActivity.this,AndroidImageSlider.class);
               startActivity(intentpost);

//                qrScan = new IntentIntegrator(Business_AccountsettingActivity.this);
//                //  qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                qrScan.setOrientationLocked(true);
//
//                // qrScan.setBeepEnabled(true);
//                // qrScan.setCaptureActivity(CaptureActivity.class);
//                //qrScan.setCaptureLayout(R.layout.custom_layout);
//              //  qrScan.setBeepEnabled(false);
//                qrScan.initiateScan();


                break;

            case R.id.user_img:

               // Crop.pickImage(Business_AccountsettingActivity.this, 1);

                break;
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_AccountsettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_AccountsettingActivity.this, 1);

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
            Toast.makeText(Business_AccountsettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public String getStringImage(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {

                Toast.makeText(this,"Result Not Found!", Toast.LENGTH_LONG).show();

            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                }
                catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    Log.e("result----",""+result.getContents());
                   Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                 //   new Json_ScanQRCode(MainActivity.this).execute(Fragment_Menu.access_tokn,result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }



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


    private void userprofile() {

        loading = new ProgressDialog(Business_AccountsettingActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.business_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                      //  Toast.makeText(Business_AccountsettingActivity.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                           name =obj.getString("name");
                            email =obj.getString("email");
                           profile_pic =obj.getString("profile_pic");
                            phone =obj.getString("phone");

                            tv_username.setText(name);
                            if(profile_pic.isEmpty()){

                            }
                            else {
                                Picasso.with(Business_AccountsettingActivity.this).load(profile_pic).into(user_img);
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
                        Toast.makeText(Business_AccountsettingActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}