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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.zoptal.blitz.model.Adv_History;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business_AdvHistory extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow;
    private ListView listview;
    private TextView tv_cmpnyname,tv_msgnohistory;

    ProgressDialog loading;
    int currentPage;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    CustomListAdapter adpaterhistory;
    private List<Adv_History> historyList = new ArrayList<Adv_History>();
    String saleheading,companycate,saledesc,storename,storeaddess;
    String ad_id;
    Boolean repost=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_history);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        initview();
    }

    public void initview(){

        Intent intent = getIntent();
        String cmpnyname=intent.getStringExtra("company_name");

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        listview=(ListView)findViewById(R.id.listview);

        adpaterhistory = new CustomListAdapter(historyList);
        listview.setAdapter(adpaterhistory);

        tv_cmpnyname=(TextView)findViewById(R.id.tv_cmpnyname);
        tv_msgnohistory=(TextView)findViewById(R.id.tv_msgnohistory);

        tv_cmpnyname.setText(cmpnyname);
         img_backarrow.setOnClickListener(this);

        adv_historylist();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_AdvHistory.this, Business_WillowClothingActivity.class);
                startActivity(i);
                finish();
                break;
//            case R.id.btn_information:
//
//                break;
//
//            case R.id.btn_payment:
//
//                break;

        }
    }

class CustomListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<Adv_History> historyItems;

    public CustomListAdapter(List<Adv_History> historyItems) {

        this.context = context;
         this.historyItems=historyItems;
        inflater = LayoutInflater.from(getApplicationContext());

        // TODO Auto-generated constructor stub
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//            if(historyItems.size()==0){
//
//                tv_msgnohistory.setVisibility(View.VISIBLE);
//            }
//            else{
//                tv_msgnohistory.setVisibility(View.GONE);
//            }
        return historyItems.size();

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holdler holder = new Holdler();
        convertView = inflater.inflate(R.layout.customview_advhistory, null);
        holder.img = (ImageView) convertView.findViewById(R.id.img_repost);
        holder.img_dlt = (ImageView) convertView.findViewById(R.id.img_dlt);
        holder.tv = (TextView) convertView.findViewById(R.id.tv_hisname);
        holder.rel_history=(RelativeLayout)convertView.findViewById(R.id.rel_history);


        holder.tv.setText(historyItems.get(position).getSale_heading());
        saleheading=historyItems.get(position).getSale_heading();
        companycate=historyItems.get(position).getComapny_category();
        saledesc=historyItems.get(position).getSale_desc();
        storename=historyItems.get(position).getStorename();
        storeaddess=historyItems.get(position).getStoreaddress();


        if(historyItems.get(position).getAd_edit().equals("0")){

         //   holder.img_dlt.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
        }
        else{

          //  holder.img_dlt.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);
        }


        if(historyItems.get(position).getAd_dlt().equals("0")){

             holder.img_dlt.setVisibility(View.GONE);

        }
        else{

            holder.img_dlt.setVisibility(View.VISIBLE);

        }

        holder.rel_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Business_AdvHistory.this, Business_AdvHistoryDetail.class);
                i.putExtra("saleheading",saleheading);
                i.putExtra("companycate",companycate);
                i.putExtra("saledesc",saledesc);
                i.putExtra("storename",storename);
                i.putExtra("storeaddess",storeaddess);
                startActivity(i);
                finish();
            }
        });


        holder.img .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repost=true;
                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                editor1.putString("repost", String.valueOf(repost));
                editor1.commit();

                ad_id=historyItems.get(position).getid();
                Intent i = new Intent(Business_AdvHistory.this, Business_CreateAdvActivity.class);
                i.putExtra("ad_id",ad_id);
                startActivity(i);

            }
        });

        holder.img_dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad_id=historyItems.get(position).getid();
                dialogdltconfirmation(position);
                adpaterhistory.notifyDataSetChanged();

            }
        });


        return convertView;


    }

}
    class Holdler
    {
        ImageView img;
        ImageView img_dlt;
        TextView tv;
        RelativeLayout rel_history;
    }

    public void dialogdltconfirmation(final int pos) {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
      final Dialog  dialog = new Dialog(Business_AdvHistory.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText("Are you sure you want to Delete?");
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

               deleteadv(pos);
                adpaterhistory.notifyDataSetChanged();

                dialog.dismiss();

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

    private void adv_historylist(){

        loading = new ProgressDialog(Business_AdvHistory.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.advertismnt_history,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                     Log.e("respose adv hostory===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Adv_History advhislistdata = new Adv_History();

                                advhislistdata.setId(obj.getString("id"));
                                advhislistdata.setComapny_category(obj.getString("company_category"));
                                advhislistdata.setSale_desc(obj.getString("sales_description"));
                                advhislistdata.setSale_heading(obj.getString("sales_heading"));
                                advhislistdata.setSdate(obj.getString("start_date"));
                                advhislistdata.setStime_hr(obj.getString("start_time_hr"));
                                advhislistdata.setStime_min(obj.getString("start_time_min"));
                                advhislistdata.setEndtime_hr(obj.getString("end_time_hr"));
                                advhislistdata.setEndtime_min(obj.getString("end_time_min"));
                                advhislistdata.setStoreid(obj.getString("store_id"));
                                advhislistdata.setOldprice(obj.getString("old_price"));
                                advhislistdata.setNewprice(obj.getString("new_price"));
                                advhislistdata.setDiscountper(obj.getString("discount_per"));
                                advhislistdata.setStorename(obj.getString("store_name"));
                                advhislistdata.setStoreaddress(obj.getString("store_address"));
                                advhislistdata.setVisitor(obj.getString("visitor"));
                                advhislistdata.setStatus(obj.getString("status"));
                                advhislistdata.setPosted(obj.getString("posted"));
                                advhislistdata.setAd_edit(obj.getString("ad_edit"));
                                advhislistdata.setAd_dlt(obj.getString("ad_delete"));

// adding storelistdata to storeList array
                                historyList.add(advhislistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adpaterhistory.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvHistory.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("access_token",access_tokn);
                params.put("page_no","1");
                params.put("no_of_post","15");


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void deleteadv(final int pos) {

        loading = new ProgressDialog(Business_AdvHistory.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.delete_ad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose dltttt===",""+response);

                        historyList.remove(pos);
                        adpaterhistory.notifyDataSetChanged();
                        // getstorelist();


//                        try {
//                            JSONObject main_obj = new JSONObject(response);
//                            JSONArray ary_store= main_obj.getJSONArray("data");
//
//                            for (int i = 0; i < ary_store.length(); i++) {
//
//                                JSONObject obj = ary_store.getJSONObject(i);
//
//                                Store_List storelistdata = new Store_List();
//
//                                storelistdata.setStore_name(obj.getString("store_name"));
//                                storelistdata.setId(obj.getString("id"));
//                                storelistdata.setStore_address(obj.getString("store_address"));
//                                storelistdata.setStore_phone(obj.getString("store_phone"));
//                                storelistdata.setStore_hours_from(obj.getString("store_hours_from"));
//                                storelistdata.setStore_hours_to(obj.getString("store_hours_to"));
//                                storelistdata.setLatitude(obj.getString("latitude"));
//                                storelistdata.setLongitude(obj.getString("longitude"));
//
//
//// adding storelistdata to storeList array
//                                storeList.add(storelistdata);
//
//                            }
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvHistory.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);
                map.put("ad_id",ad_id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

