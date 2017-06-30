package com.zoptal.blitz.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.main.MainActivity1;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Fragment_ChangePw extends Fragment implements View.OnClickListener {

    MainActivity1 activity1;
    View view;
    ProgressDialog loading;
    private EditText ed_oldpw,ed_newpw,ed_oldemail,ed_newemail;
    private Button btn_confirm;

    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment_buyer_changepw, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

         activity1=(MainActivity1)getActivity();
        activity1.mDrawerToggle.setDrawerIndicatorEnabled(false);
        activity1.mDrawerToggle.setHomeAsUpIndicator(null);
         activity1.textToolHeader.setText("    Change Password");
         activity1.tv_bck.setVisibility(View.VISIBLE);
        activity1.img_dltall.setVisibility(View.GONE);

        activity1.img_star.setVisibility(View.GONE);

        activity1.tv_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment7=new Fragment_BuyerAccountsetting();
                FragmentManager fragmentManager7 =getFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.activity_main_content_fragment, fragment7).commit();
                activity1.Drawer.closeDrawer(Gravity.LEFT);
            }
        });

          initview();

        return view;

    }

    private void initview() {


        ed_oldpw=(EditText)view.findViewById(R.id.ed_oldpw);
        ed_newpw=(EditText)view.findViewById(R.id.ed_newpw);
        ed_oldemail=(EditText)view.findViewById(R.id.ed_oldemail);
        ed_newemail=(EditText)view.findViewById(R.id.ed_newemail);

        btn_confirm=(Button)view.findViewById(R.id.btn_confirm);


         btn_confirm.setOnClickListener(this);
        sharedpreferences1 =getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_confirm:

                if (NetworkConnection.isConnectedToInternet(getActivity())) {

                    changepw();

                }
                else {
                    Toast.makeText(getActivity(),"Please Check your internet connection", Toast.LENGTH_SHORT).show();
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


        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
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
                                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                String message =main_obj.getString("message");
                                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                ed_oldpw.setText("");
                                ed_newpw.setText("");
                                ed_oldemail.setText("");
                                ed_newemail.setText("");
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
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("old_password",oldpw);
                params.put("password",newpw);
                params.put("old_email", oldemail);
                params.put("new_email", newemail);
                params.put("access_token",buyer_access_token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}