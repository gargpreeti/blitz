package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.zoptal.blitz.model.Rating_list;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business_Stats extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow;
    private TextView tv_companyname,tv_view,tv_codeopened,tv_stime,tv_etime,tv_msg;
     private Button btn_previousadv;

    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    ListView listview_rating;
    CustomList_ratinglist  adapter_rating;
    private List<Rating_list> ratelist = new ArrayList<Rating_list>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_stats);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }


    public void initview(){

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        tv_companyname=(TextView)findViewById(R.id.tv_companyname);
        tv_view=(TextView)findViewById(R.id.tv_view);
        tv_codeopened=(TextView)findViewById(R.id.tv_codeopened);
        tv_stime=(TextView)findViewById(R.id.tv_stime);
        tv_etime=(TextView)findViewById(R.id.tv_etime);
        tv_msg=(TextView)findViewById(R.id.tv_msg);
        listview_rating=(ListView)findViewById(R.id.listview_rating);

        adapter_rating = new CustomList_ratinglist(Business_Stats.this,ratelist);

        listview_rating.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        btn_previousadv=(Button)findViewById(R.id.btn_previousadv);

        img_backarrow.setOnClickListener(this);
        btn_previousadv.setOnClickListener(this);

        business_stat();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_Stats.this, Business_WillowClothingActivity.class);
                startActivity(i);
                finish();
             break;

            case R.id.btn_previousadv:

                Intent intent = new Intent(Business_Stats.this, Business_AdvHistory.class);
                startActivity(intent);

              break;

//            case R.id.btn_payment:
//
//                break;

        }
    }


    private void business_stat() {

        loading = new ProgressDialog(Business_Stats.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.business_stat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("response===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                            String company_name  =obj.getString("company_name");
                            String popular_time_from =obj.getString("popular_time_from");
                            String  popularTimeTo =obj.getString("popular_time_to");
                            String  total_views =obj.getString("total_views");
                            String  tcode_views =obj.getString("tcode_views");
                            String  avg_rating =obj.getString("avg_rating");

                            JSONArray ary_rating= obj.getJSONArray("rating");

                            for (int i = 0; i < ary_rating.length(); i++) {

                                JSONObject objrating = ary_rating.getJSONObject(i);

                                Rating_list ratinglistdata = new Rating_list();

                                ratinglistdata.setRating(objrating.getString("rating"));
                                ratinglistdata.setComment(objrating.getString("comment"));
                                ratinglistdata.setRating_by(objrating.getString("rating_by"));
                                ratinglistdata.setProfile_pic(objrating.getString("profile_pic"));

                                ratelist.add(ratinglistdata);



                            }
                            listview_rating.setAdapter(adapter_rating);




                            tv_companyname.setText(company_name);
                            tv_view.setText(total_views);
                            tv_codeopened.setText(tcode_views+"  "+"Times");
                            tv_stime.setText(popular_time_from);
                            tv_etime.setText(popularTimeTo);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }




                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_Stats.this,error.toString(),Toast.LENGTH_LONG ).show();
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
    public class CustomList_ratinglist extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        private List<Rating_list> ratinglist;

        public CustomList_ratinglist(Context context, List<Rating_list> ratinglist) {

            this.context = context;
            this.ratinglist = ratinglist;
            inflater = LayoutInflater.from(context);

            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            //   Log.e("homefeedsize",""+homefeed.size());
//            if(homefeed.size()==0){
//                tv_msg.setVisibility(View.VISIBLE);
//
//            }
//            else{
//                tv_msg.setVisibility(View.GONE);
//
//            }
            Log.e("rating list size=====",""+ratinglist.size());
            return ratinglist.size();

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

            final Holdlerrating holder = new Holdlerrating();
            convertView = inflater.inflate(R.layout.customview_ratinglist_buyer, null);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_star5 = (ImageView) convertView.findViewById(R.id.img_star5);
            holder.img_star4 = (ImageView) convertView.findViewById(R.id.img_star4);
            holder.img_star3 = (ImageView) convertView.findViewById(R.id.img_star3);
            holder.img_star2 = (ImageView) convertView.findViewById(R.id.img_star2);
            holder.img_star1 = (ImageView) convertView.findViewById(R.id.img_star1);
            holder.ed_desc = (EditText) convertView.findViewById(R.id.ed_desc);


            holder.tv_name.setText(ratinglist.get(position).getRating_by());
            holder.ed_desc.setText(ratinglist.get(position).getComment());
            //   holder.tv_colr.setBackgroundColor(Color.parseColor(homefeed.get(position).getColor_code()));

//            if(ratinglist.get(position).getRating().equals("1")){
//                holder.img_star1.setBackgroundResource(R.drawable.star);
//                holder.img_star2.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star3.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star4.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star5.setBackgroundResource(R.mipmap.star_2);
//
//            }
//            if(ratinglist.get(position).getRating().equals("2")){
//
//                holder.img_star1.setBackgroundResource(R.drawable.star);
//                holder.img_star2.setBackgroundResource(R.drawable.star);
//                holder.img_star3.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star4.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star5.setBackgroundResource(R.mipmap.star_2);
//            }
//
//            if(ratinglist.get(position).getRating().equals("3")){
//
//                holder.img_star1.setBackgroundResource(R.drawable.star);
//                holder.img_star2.setBackgroundResource(R.drawable.star);
//                holder.img_star3.setBackgroundResource(R.drawable.star);
//                holder.img_star4.setBackgroundResource(R.mipmap.star_2);
//                holder.img_star5.setBackgroundResource(R.mipmap.star_2);
//            }
//
//
//            if(ratinglist.get(position).getRating().equals("4")){
//
//                holder.img_star1.setBackgroundResource(R.drawable.star);
//                holder.img_star2.setBackgroundResource(R.drawable.star);
//                holder.img_star3.setBackgroundResource(R.drawable.star);
//                holder.img_star4.setBackgroundResource(R.drawable.star);
//                holder.img_star5.setBackgroundResource(R.mipmap.star_2);
//            }
//
//            if(ratinglist.get(position).getRating().equals("5")){
//
//                holder.img_star1.setBackgroundResource(R.drawable.star);
//                holder.img_star2.setBackgroundResource(R.drawable.star);
//                holder.img_star3.setBackgroundResource(R.drawable.star);
//                holder.img_star4.setBackgroundResource(R.drawable.star);
//                holder.img_star5.setBackgroundResource(R.drawable.star);
//            }


            if(ratinglist.get(position).getRating().equals("0.50")){
                holder.img_star1.setBackgroundResource(R.mipmap.half);
                holder.img_star2.setBackgroundResource(R.mipmap.disable);
                holder.img_star3.setBackgroundResource(R.mipmap.disable);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);

            }

            if(ratinglist.get(position).getRating().equals("1.00")){
                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.disable);
                holder.img_star3.setBackgroundResource(R.mipmap.disable);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);

            }
            if(ratinglist.get(position).getRating().equals("1.50")){
                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.half);
                holder.img_star3.setBackgroundResource(R.mipmap.disable);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);

            }
            if(ratinglist.get(position).getRating().equals("2.00")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.disable);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);
            }
            if(ratinglist.get(position).getRating().equals("2.50")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.half);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);
            }
            if(ratinglist.get(position).getRating().equals("3.00")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.star);
                holder.img_star4.setBackgroundResource(R.mipmap.disable);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);
            }

            if(ratinglist.get(position).getRating().equals("3.50")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.star);
                holder.img_star4.setBackgroundResource(R.mipmap.half);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);
            }
            if(ratinglist.get(position).getRating().equals("4.00")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.star);
                holder.img_star4.setBackgroundResource(R.mipmap.star);
                holder.img_star5.setBackgroundResource(R.mipmap.disable);
            }
            if(ratinglist.get(position).getRating().equals("4.50")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.star);
                holder.img_star4.setBackgroundResource(R.mipmap.star);
                holder.img_star5.setBackgroundResource(R.mipmap.half);
            }
            if(ratinglist.get(position).getRating().equals("5.00")){

                holder.img_star1.setBackgroundResource(R.mipmap.star);
                holder.img_star2.setBackgroundResource(R.mipmap.star);
                holder.img_star3.setBackgroundResource(R.mipmap.star);
                holder.img_star4.setBackgroundResource(R.mipmap.star);
                holder.img_star5.setBackgroundResource(R.mipmap.star);
            }



            adapter_rating.notifyDataSetChanged();
            return convertView;

        }

        class Holdlerrating {
            TextView tv_name;
            ImageView img_star5;
            ImageView img_star4;
            ImageView img_star3;
            ImageView img_star2;
            ImageView img_star1;
            EditText ed_desc;
        }
    }
}
