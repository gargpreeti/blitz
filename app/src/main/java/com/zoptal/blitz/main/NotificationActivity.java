package com.zoptal.blitz.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.model.Notification_List;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    ListView listview;
    TextView tv_msgnotification;
    private ImageView img_backarrow,img_dltall;
    Dialog dialog;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_token;
    ProgressDialog loading;
    CustomListAdapternoti adapterfav;
    private List<Notification_List> notificationList = new ArrayList<Notification_List>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_token = sharedpreferences1.getString("access_token", "");
        init();

    }

    private void init() {

        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
        img_dltall= (ImageView) findViewById(R.id.img_dltall);
        listview=(ListView)findViewById(R.id.listview);
        tv_msgnotification=(TextView)findViewById(R.id.tv_msgnotification);
        // listview.setAdapter(new CustomListAdapterFav(favList));

        adapterfav = new CustomListAdapternoti(notificationList);
        listview.setAdapter(adapterfav);


        img_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NotificationActivity.this, Business_WillowClothingActivity.class);
                startActivity(i);
                finish();
            }
        });

        img_dltall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogconfirmall();
                adapterfav.notifyDataSetChanged();

            }
        });




        getnotilist();
        // notificationread();
    }

    public void dialogconfirmall() {

        final  Dialog  dialog = new Dialog(NotificationActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_logout);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);
        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);

        tv_msg.setText("Are you sure you want to delete all notifications?");



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deltenotificationall();
                adapterfav.notifyDataSetChanged();
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
    class CustomListAdapternoti extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        // String [] historyname;

        private List<Notification_List> notiItems;

        public CustomListAdapternoti(List<Notification_List> notiItems) {

            this.context = context;
            this.notiItems=notiItems;
            inflater = LayoutInflater.from(NotificationActivity.this);

            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            if(notiItems.size()==0){

                tv_msgnotification.setVisibility(View.VISIBLE);
                img_dltall.setVisibility(View.GONE);
            }
            else{
                img_dltall.setVisibility(View.VISIBLE);
                tv_msgnotification.setVisibility(View.GONE);
            }

            return notiItems.size();
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

            Holdlerfav holder = new Holdlerfav();
            convertView = inflater.inflate(R.layout.customview_notification_list, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv_name1);
            holder.rel_list = (RelativeLayout) convertView.findViewById(R.id.rel_list);
            holder.img_date = (ImageView) convertView.findViewById(R.id.img_date);
            holder.img_time = (ImageView) convertView.findViewById(R.id.img_time);
            holder.img_dlt = (ImageView) convertView.findViewById(R.id.img_dlt);


            String result=notiItems.get(position).getMessage();
            String contnt = Html.fromHtml(result).toString();

            if(contnt.length()>33){

                contnt=contnt.substring(0,32)+". . .";

                holder.tv1.setText(contnt);
            }else{

                holder.tv1.setText(contnt);

            }

            holder.tv.setText(notiItems.get(position).getStore_name());
            holder.tv_time.setText(notiItems.get(position).getTym());
            holder.tv_date.setText(notiItems.get(position).getDt());



            //  holder.tv1.setText(notiItems.get(position).getMessage());

         if(notiItems.get(position).getStatus().equals("1")){
                holder.tv.setTextColor(Color.parseColor("#c1c0bd"));
                holder.tv1.setTextColor(Color.parseColor("#c1c0bd"));
                holder.tv_time.setTextColor(Color.parseColor("#c1c0bd"));
                holder.tv_date.setTextColor(Color.parseColor("#c1c0bd"));

             holder.img.setBackgroundResource(R.mipmap.notification);
             holder.img_date.setBackgroundResource(R.drawable.calendarx);
             holder.img_time.setBackgroundResource(R.drawable.timex);
             holder.rel_list.setBackgroundColor(Color.parseColor("#ffffff"));

            }

            holder.rel_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    id=notiItems.get(position).getNotification_id();

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(NotificationActivity.this,notiItems.get(position).getMessage());

                    alertmsg.dialog.show();
                    notificationread();
                }
            });

            holder.img_dlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    id=notiItems.get(position).getNotification_id();
                    dialogconfirm(position);
                    adapterfav.notifyDataSetChanged();

                }
            });


            return convertView;

        }




        public void dialogconfirm(final int pos) {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
            final  Dialog  dialog = new Dialog(NotificationActivity.this, android.R.style.Theme_Translucent);
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_logout);

            ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
            TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
            TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);
            TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);

            tv_msg.setText("Are you sure you want to delete ?");



            img_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.dismiss();
                }
            });
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deltenotificationsingle(pos);
                    adapterfav.notifyDataSetChanged();
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
    }
    class Holdlerfav
    {
        ImageView img;
        TextView tv;
        TextView tv1;
        TextView tv_time;
        TextView tv_date;
        RelativeLayout rel_list;
        ImageView img_date;
        ImageView img_time;
        ImageView img_dlt;
    }

    private void getnotilist() {

        loading = new ProgressDialog(NotificationActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.notification_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Notification_List notilistdata = new Notification_List();

                                notilistdata.setNotification_id(obj.getString("notification_id"));
                                notilistdata.setSender_id("sender_id");
                                notilistdata.setSender_status(obj.getString("sender_status"));
                                notilistdata.setStore_name(obj.getString("store_name"));
                                notilistdata.setMessage(obj.getString("message"));
                                notilistdata.setType(obj.getString("type"));
                                notilistdata.setAd_id(obj.getString("ad_id"));
                                notilistdata.setDate(obj.getString("date"));
                                notilistdata.setStatus(obj.getString("status"));
                                notilistdata.setDt(obj.getString("date"));
                                notilistdata.setTym(obj.getString("time"));


// adding storelistdata to storeList array
                                notificationList.add(notilistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterfav.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_token);
                map.put("page_no","1");
                map.put("no_of_post","50");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        requestQueue.add(stringRequest);
    }

    private void notificationread() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.notification_read,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("respose read===",""+response);

                        Intent i = new Intent(getApplication(),NotificationActivity.class);
                        startActivity(i);



                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                // map.put("access_token",buyer_access_token);
                map.put("notification_id",id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        requestQueue.add(stringRequest);
    }




    private void deltenotificationsingle(final int pos) {

        loading = new ProgressDialog(NotificationActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.delete_single_notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        notificationList.remove(pos);
                        Log.e("respose dltttt===",""+response);
                        adapterfav.notifyDataSetChanged();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_token);
                map.put("id",id);

                Log.e("mapppppp",""+map);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void deltenotificationall() {

        loading = new ProgressDialog(NotificationActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.delete_all_notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        notificationList.clear();
                        Log.e("respose dltttt all===",""+response);
                        adapterfav.notifyDataSetChanged();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_token);

                Log.e("mapppppp",""+map);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
