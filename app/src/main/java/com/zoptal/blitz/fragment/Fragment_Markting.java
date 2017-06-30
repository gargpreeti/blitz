package com.zoptal.blitz.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.main.MainActivity1;
import com.zoptal.blitz.main.Payment_ChartActivity;
import com.zoptal.blitz.model.Leader_list;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Markting extends Fragment implements View.OnClickListener {

    MainActivity1 activity1;
    View view;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token;
    private TextView tv_totalsales,tv_totalposts,tv_totalamount,tv_code;
    private EditText ed_email,ed_amount;
    private Button btn_withdraw,btn_leaderboard,btn_paymentchart;
    ListView listview;
    CustomListAdapterleader adapter;
    private List<Leader_list> ledrList = new ArrayList<Leader_list>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment_buyer_marketing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

         activity1=(MainActivity1)getActivity();
        activity1.mDrawerToggle.setDrawerIndicatorEnabled(false);
        activity1.mDrawerToggle.setHomeAsUpIndicator(null);
         activity1.textToolHeader.setText("     Card Details");
         activity1.tv_bck.setVisibility(View.VISIBLE);
        activity1.img_star.setVisibility(View.GONE);
        activity1.img_dltall.setVisibility(View.GONE);


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

        tv_totalsales=(TextView)view.findViewById(R.id.tv_totalsales);
        tv_totalposts=(TextView)view.findViewById(R.id.tv_totalposts);
        tv_totalamount=(TextView)view.findViewById(R.id.tv_totalamount);
        tv_code=(TextView)view.findViewById(R.id.tv_code);
        ed_email=(EditText)view.findViewById(R.id.ed_email);
        ed_amount=(EditText)view.findViewById(R.id.ed_amount);

        btn_withdraw=(Button)view.findViewById(R.id.btn_withdraw);
        btn_leaderboard=(Button)view.findViewById(R.id.btn_leaderboard);
        btn_paymentchart=(Button)view.findViewById(R.id.btn_paymentchart);

        sharedpreferences1 =getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

        btn_withdraw.setOnClickListener(this);
        btn_leaderboard.setOnClickListener(this);
        btn_paymentchart.setOnClickListener(this);

        get_carddetails();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_withdraw:
                if (NetworkConnection.isConnectedToInternet(getActivity())) {

                    getwithdraw();
                }
                else {
                    Toast.makeText(getActivity(),"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
             break;

            case R.id.btn_leaderboard:


                if (NetworkConnection.isConnectedToInternet(getActivity())) {

                    dialoglist();
                }
                else {
                    Toast.makeText(getActivity(),"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;


            case R.id.btn_paymentchart:

                Intent intent_stats = new Intent(getActivity(),Payment_ChartActivity.class);
                startActivity(intent_stats);

                break;

        }
    }

    private void get_carddetails() {

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.get_buyer_stats,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);

                            String totalsales=main_obj.getString("total_earning");
                            String totalposts=main_obj.getString("total_ad");
                            String totalamount=main_obj.getString("valut_amount");
                            String paypal_email=main_obj.getString("paypal_email");
                            String promo_code=main_obj.getString("promo_code");

                            tv_totalsales.setText(totalsales);
                            tv_totalposts.setText(totalposts);
                            tv_totalamount.setText("$ "+totalamount);
                            ed_email.setText(paypal_email);
                            tv_code.setText(promo_code);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getwithdraw() {

        final String email=ed_email.getText().toString().trim();
        final String amount=ed_amount.getText().toString().trim();

        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(getActivity(), "Please provide email first.");

            alertmsg.dialog.show();
            return;
        }

        if(amount.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(getActivity(), "Please provide amount.");

            alertmsg.dialog.show();
            return;
        }



        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.get_fund_buyer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);

                            String msg=main_obj.getString("message");

                            AlertDialogMsg alertmsg = new
                                    AlertDialogMsg(getActivity(), msg);

                            alertmsg.dialog.show();



                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
         },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("email",email);
                map.put("amount",amount);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void leader_board() {

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.leader_board,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                          Log.e("respose leader===",""+response);
                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Leader_list leaderlistdata = new Leader_list();

                                leaderlistdata.setBuyer_id(obj.getString("buyer_id"));
                                leaderlistdata.setName(obj.getString("name"));
                                leaderlistdata.setProfile_pic(obj.getString("profile_pic"));
                                leaderlistdata.setTamount(obj.getString("tamount"));



// adding storelistdata to storeList array
                              ledrList.add(leaderlistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
      },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void dialoglist() {

       final Dialog  dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_leaderlist);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        listview=(ListView)dialog.findViewById(R.id.listview);
        adapter = new CustomListAdapterleader(ledrList);
        listview.setAdapter(adapter);
        leader_board();

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    class CustomListAdapterleader extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        // String[] historyname;

        private List<Leader_list> leaderItems;

        public CustomListAdapterleader(List<Leader_list> leaderItems) {

            this.context = context;
            this.leaderItems = leaderItems;
            inflater = LayoutInflater.from(getActivity());

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub


            return leaderItems.size();

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub

            Holdlerleader holder = new Holdlerleader();
            convertView = inflater.inflate(R.layout.customview_leaderlist, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img_user);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tamount);


            holder.tv_name.setText(leaderItems.get(position).getName());
            holder.tv_amount.setText(leaderItems.get(position).getTamount());

            return convertView;

        }

    }


    class Holdlerleader
    {
        ImageView img;
        TextView tv_name;
        TextView tv_amount;

    }


}