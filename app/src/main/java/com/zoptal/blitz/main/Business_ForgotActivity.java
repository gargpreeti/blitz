package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Business_ForgotActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText ed_email;
    private ImageView img_backarrow;
    private Button btn_submit;

    ProgressDialog  loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__forgotpw);

        initview();
    }


    public void initview(){


        ed_email=(EditText)findViewById(R.id.ed_email);
        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        btn_submit=(Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:
                Intent i = new Intent(Business_ForgotActivity.this, Business_SigninActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_submit:

                if (NetworkConnection.isConnectedToInternet(Business_ForgotActivity.this)) {

                   forgotpw();

                }
                else {
                    Toast.makeText(Business_ForgotActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

                break;

        }
    }


    private void forgotpw() {

        final String email=ed_email.getText().toString().trim();

        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_ForgotActivity.this, "Please enter your email.");

            alertmsg.dialog.show();
            return;
        }

        loading = new ProgressDialog(Business_ForgotActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.forgot_password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg = main_obj.getString("message");

                            Toast.makeText(Business_ForgotActivity.this,msg,Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // Toast.makeText(Business_ForgotActivity.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_ForgotActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("email",email);
                map.put("user_type","1");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }






}
