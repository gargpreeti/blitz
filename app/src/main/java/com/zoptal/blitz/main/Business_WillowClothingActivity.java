package com.zoptal.blitz.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Business_WillowClothingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView relative_logout;
    private TextView tv_account,tv_companyinfo,tv_remaining;
    private ImageView img_profile,img_createadv,img_advbalance,img_advhistory,img_stats,img_notification;
    private Dialog dialog;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    String  points,company_name,business_info,store_info;
    Boolean repost=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__willowclothing);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }

    public void initview(){

        relative_logout=(TextView)findViewById(R.id.relative_logout);
        tv_account=(TextView)findViewById(R.id.tv_account);
        tv_companyinfo=(TextView)findViewById(R.id.tv_companyinfo);
        tv_remaining=(TextView)findViewById(R.id.tv_remaining);
        img_profile=(ImageView) findViewById(R.id.img_profile);
        img_notification=(ImageView) findViewById(R.id.img_notification);
        img_createadv=(ImageView) findViewById(R.id.img_createadv);
        img_advbalance=(ImageView) findViewById(R.id.img_advbalance);
        img_advhistory=(ImageView) findViewById(R.id.img_advhistory);
        img_stats=(ImageView) findViewById(R.id.img_stats);


        relative_logout.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        img_createadv.setOnClickListener(this);
        img_advbalance.setOnClickListener(this);
        img_advhistory.setOnClickListener(this);
        img_stats.setOnClickListener(this);
        img_notification.setOnClickListener(this);

        getinfo();
        notiinfo();

    }

    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        this.moveTaskToBack(true);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.relative_logout:

                dialoglogout();

                break;

            case R.id.tv_account:

                Intent intent = new Intent(Business_WillowClothingActivity.this, Business_AccountsettingActivity.class);
                startActivity(intent);


                break;
            case R.id.img_profile:

                Intent intent1 = new Intent(Business_WillowClothingActivity.this, Business_AccountsettingActivity.class);
                startActivity(intent1);


                break;

            case R.id.img_createadv:

                if(business_info.equals("0")){

                    dialogcreateadv();

                }
                else if(store_info.equals("0")){

                    dialogstoreadd();
                }
                else if(points.equals("0")){

                    dialogpointsadd();
                }
                else {

                    repost=false;
                    SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                    editor1.putString("repost", String.valueOf(repost));
                    editor1.commit();


                    Intent intent_adv = new Intent(Business_WillowClothingActivity.this, Business_CreateAdvActivity.class);
                    startActivity(intent_adv);

                }
                break;

            case R.id.img_advbalance:

                Intent intent_bal = new Intent(Business_WillowClothingActivity.this, Business_AdvBalance.class);
                intent_bal.putExtra("points",points);
                startActivity(intent_bal);

                break;
            case R.id.img_advhistory:

                Intent intent_history = new Intent(Business_WillowClothingActivity.this, Business_AdvHistory.class);
                intent_history.putExtra("company_name",company_name);
                startActivity(intent_history);

                break;
            case R.id.img_stats:

                Intent intent_stats = new Intent(Business_WillowClothingActivity.this, Business_Stats.class);
                startActivity(intent_stats);

                break;
            case R.id.img_notification:

                Intent intent_notification = new Intent(Business_WillowClothingActivity.this, NotificationActivity.class);
                startActivity(intent_notification);

                break;

        }
    }



    public void dialoglogout() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_WillowClothingActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

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

                userLogout();
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

    public void dialogcreateadv() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_WillowClothingActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_alert);

        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText("Please first add Business information");

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_ok=(TextView)dialog.findViewById(R.id.tv_ok);
       // TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentbusiness = new Intent(Business_WillowClothingActivity.this, Business_businesssettingActivity.class);
                startActivity(intentbusiness);

              dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void dialogstoreadd() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_WillowClothingActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_alertstore);

        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(" Please first add store information");

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_ok=(TextView)dialog.findViewById(R.id.tv_ok);
        // TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentstore = new Intent(Business_WillowClothingActivity.this, Business_businessstoreActivity.class);
                startActivity(intentstore);

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void dialogpointsadd() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_WillowClothingActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_alertstore);

        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(" Please first add balance");

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_ok=(TextView)dialog.findViewById(R.id.tv_ok);
        // TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_bal = new Intent(Business_WillowClothingActivity.this, Business_AdvBalance.class);
                intent_bal.putExtra("points",points);
                startActivity(intent_bal);
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    private void userLogout() {

        loading = new ProgressDialog(Business_WillowClothingActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.logout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(Business_WillowClothingActivity.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                            Log.e("respose===",""+response);


                            SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                            editor1.putString("id","");
                            editor1.putString("access_token","");
                            editor1.commit();

                        Intent i = new Intent(Business_WillowClothingActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_WillowClothingActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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

    private void notiinfo() {

//        loading = new ProgressDialog(Business_WillowClothingActivity.this,R.style.AppCompatAlertDialogStyle);
//        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.notification_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //  loading.dismiss();

                        Log.e("respose===",""+response);



                        try {
                            JSONObject main_obj = new JSONObject(response);

                            String noti = main_obj.getString("notification");

                            if(noti.equals("0")){

                                img_notification.setBackgroundResource(R.mipmap.notification);
                            }
                            else{

                                img_notification.setBackgroundResource(R.drawable.notificationgold);
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
                        Toast.makeText(Business_WillowClothingActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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
    private void getinfo() {

//        loading = new ProgressDialog(Business_WillowClothingActivity.this,R.style.AppCompatAlertDialogStyle);
//        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.get_company_name_points,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("message");

                         company_name  =obj.getString("company_name");
                           business_info =obj.getString("business_info");
                            points=obj.getString("points");
                        store_info =obj.getString("store_info");

                            tv_companyinfo.setText(company_name);
                            tv_remaining.setText("Remaining Credits:"+" "+points);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_WillowClothingActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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
