package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Business_ChangePwActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow;;
    private EditText ed_oldpw,ed_newpw,ed_oldemail,ed_newemail;
    private Button btn_confirm;
    ProgressDialog loading;

    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__changepw);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        initview();
    }


    public void initview(){

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        ed_oldpw=(EditText)findViewById(R.id.ed_oldpw);
        ed_newpw=(EditText)findViewById(R.id.ed_newpw);
        ed_oldemail=(EditText)findViewById(R.id.ed_oldemail);
        ed_newemail=(EditText)findViewById(R.id.ed_newemail);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);


        img_backarrow.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_ChangePwActivity.this, Business_AccountsettingActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_confirm:

                if (NetworkConnection.isConnectedToInternet(Business_ChangePwActivity.this)) {
                    changepw();

                }
                else {
                    Toast.makeText(Business_ChangePwActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

                break;

        }
    }

    private void changepw(){

        final String oldpw = ed_oldpw.getText().toString().trim();
        final String newpw = ed_newpw.getText().toString().trim();
        final String oldemail= ed_oldemail.getText().toString().trim();
        final String newemail = ed_newemail.getText().toString().trim();


        loading = new ProgressDialog(Business_ChangePwActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.changepw,
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
                             //   Toast.makeText(Business_ChangePwActivity.this,message,Toast.LENGTH_LONG).show();

                                AlertDialogMsg alertmsg = new
                                        AlertDialogMsg(Business_ChangePwActivity.this,message);

                                alertmsg.dialog.show();

                                return;
                            }
                                 else {
                                String message =main_obj.getString("message");
                                ed_oldpw.setText("");
                                ed_newpw.setText("");
                                ed_oldemail.setText("");
                                ed_newemail.setText("");
                              //  Toast.makeText(Business_ChangePwActivity.this,message,Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Business_ChangePwActivity.this, Business_AccountsettingActivity.class);
                                startActivity(i);
                                finish();

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
                        Toast.makeText(Business_ChangePwActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("old_password",oldpw);
                params.put("password",newpw);
                params.put("old_email", oldemail);
                params.put("new_email", newemail);
                params.put("access_token",access_tokn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
