package com.zoptal.blitz.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.model.Images_List;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business_AdvPostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_cmpnyname,tv_storename,tv_time,tv_desc,tv_address,tv_web,tv_spweb,tv_phn,tv_st,tv_end,tv_open,tv_discount;
    private ImageView img_backarrow;
     private Button btn_createadv;
    private RelativeLayout rel_web,rel_spcialweb;
     Dialog dialog;
    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    String access_tokn,repost;
    String saledec,saleheading,dt,strttym,endtym,discount,store_id,selctstore,ad_id;
    String curnttym;
    private List<Images_List> imagelist = new ArrayList<Images_List>();
    Custom_pageradapterimages mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postadv);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        repost = sharedpreferences1.getString("repost", "");

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Business_AdvPostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Business_AdvPostActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                Log.e("lat---",""+latitude);
                Log.e("long---",""+longitude);

                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        initview();
    }


    public void initview(){

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        btn_createadv=(Button)findViewById(R.id.btn_createadv);

        tv_cmpnyname=(TextView)findViewById(R.id.tv_cmpnyname);
        tv_storename=(TextView)findViewById(R.id.tv_storename);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_desc=(TextView)findViewById(R.id.tv_desc);
        tv_address=(TextView)findViewById(R.id.tv_address);
        tv_web=(TextView)findViewById(R.id.tv_web);
        tv_spweb=(TextView)findViewById(R.id.tv_spweb);
        tv_phn=(TextView)findViewById(R.id.tv_phn);
        tv_st=(TextView)findViewById(R.id.tv_st);
        tv_end=(TextView)findViewById(R.id.tv_end);
        tv_open=(TextView)findViewById(R.id.tv_open);
        tv_discount=(TextView)findViewById(R.id.tv_discount);

        rel_web=(RelativeLayout)findViewById(R.id.rel_web);
        rel_spcialweb=(RelativeLayout)findViewById(R.id.rel_spcialweb);

          img_backarrow.setOnClickListener(this);
          btn_createadv.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        // new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm a");
        curnttym= df3.format(c.getTime());
        Log.e("current date tym post--",""+curnttym);


        Intent intent = getIntent();
        saledec=intent.getStringExtra("saledec");
        saleheading=intent.getStringExtra("saleheading");
        dt=intent.getStringExtra("dt");
        strttym=intent.getStringExtra("strttym");
        endtym=intent.getStringExtra("endtym");
        discount=intent.getStringExtra("discount");
        store_id=intent.getStringExtra("store_id");
        selctstore=intent.getStringExtra("selctstore");
        ad_id=intent.getStringExtra("ad_id");

        Log.e("adid======",""+ad_id);
//       if(ad_id.equals("null")){
//           ad_id="";
//       }

        Log.e("saledesc--",""+saledec);
        Log.e("saleheading--",""+saleheading);
        Log.e("dt--",""+dt);
        Log.e("strttym--",""+strttym);
        Log.e("endtym--",""+endtym);
        Log.e("discount--",""+discount);
        Log.e("store_id--",""+store_id);

        tv_time.setText(strttym);
        tv_discount.setText(discount);

        mCustomPagerAdapter = new Custom_pageradapterimages(Business_AdvPostActivity.this, imagelist);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        get_prev_advdetail();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

//                Intent i = new Intent(Business_PostAdvActivity.this, Business_WillowClothingActivity.class);
//                startActivity(i);
                finish();
                break;

            case R.id.btn_createadv:

                create_adv();

                break;

        }
    }


    private void create_adv(){

        loading = new ProgressDialog(Business_AdvPostActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        if(ad_id==null){
            ad_id="";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_advertisement,
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

                                AlertDialogMsg alertmsg = new
                                        AlertDialogMsg(Business_AdvPostActivity.this, message);

                                alertmsg.dialog.show();

                                return;
                            }
                            else {
                                String message =main_obj.getString("message");

//                                AlertDialogMsg alertmsg = new
//                                        AlertDialogMsg(Business_AdvPostActivity.this, message);
//
//                                alertmsg.dialog.show();
                                Toast.makeText(Business_AdvPostActivity.this,message.toString(),Toast.LENGTH_LONG ).show();

                                Intent intentpost= new Intent(Business_AdvPostActivity.this, Business_WillowClothingActivity.class);
                                startActivity(intentpost);


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
                        Toast.makeText(Business_AdvPostActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sales_description",saledec);
                params.put("sales_heading",saleheading);
                params.put("start_date", dt);
                params.put("start_hr_min",strttym);
                params.put("end_hr_min", endtym);
                params.put("discount_code", discount);
                params.put("access_token",access_tokn);
                params.put("device_time",curnttym);
                params.put("store_id",store_id);
                params.put("id",ad_id);

                Log.e("param-------",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    class Custom_pageradapterimages extends PagerAdapter {

        private List<Images_List> packageItems;
        Context mContext;
        LayoutInflater mLayoutInflater;

//        int[] mResources = {
//                R.drawable.noimage,
//                R.drawable.noimage,
//                R.drawable.noimage,
//                R.drawable.noimage,
//
//        };

        public Custom_pageradapterimages(Context context,List<Images_List> packageItems) {
            mContext = context;
            this.packageItems=packageItems;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return packageItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }


        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_itemimages, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

            if(packageItems.get(position).getImage().isEmpty()){

            }
            else {
                Picasso.with(Business_AdvPostActivity.this).load(packageItems.get(position).getImage()).resize(800, 500).into(imageView);
            }

            //    imageView.setBackgroundResource(Integer.parseInt(packageItems.get(position).getImage()));

            //  imageView.setBackgroundResource(mResources[position]);



            container.addView(itemView);

            //     mCustomPagerAdapter.notifyDataSetChanged();
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    private void get_prev_advdetail() {

        loading = new ProgressDialog(Business_AdvPostActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.preview_feed_detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose previewww===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                            String cmpnycategory  =obj.getString("company_category");
                            String cmpnywebsite =obj.getString("company_website");
                            String  specialwebsite =obj.getString("special_website");
                            String  description =obj.getString("description");
                            String  cmpnyname =obj.getString("company_name");
                            String  storename =obj.getString("store_name");
                            String storeaddress=obj.getString("store_address");
                            String storephn=obj.getString("store_phone");
                            String storhourfrm=obj.getString("store_hours_from");
                            String storeHoursTo=obj.getString("store_hours_to");
                            String storopen=obj.getString("store_open");


                            if(cmpnywebsite.isEmpty()){
                                rel_web.setVisibility(View.GONE);
                            }
                            if(specialwebsite.isEmpty()){
                                rel_spcialweb.setVisibility(View.GONE);
                            }


                            tv_cmpnyname.setText(cmpnyname);

                            tv_storename.setText(saledec);
                            tv_desc.setText(description);
                            //     ed_retails.set
                            tv_address.setText(storeaddress);
                            tv_web.setText(cmpnywebsite);
                            tv_spweb.setText(specialwebsite);
                            tv_phn.setText(storephn);
                            tv_st.setText(storhourfrm+" "+"-");
                            tv_end.setText(storeHoursTo);

                            if(storopen.equals("0")){
                                tv_open.setText("Closed now");
                            }
                            else{

                                tv_open.setText("Open now");
                            }

                            JSONArray ary_imgs= obj.getJSONArray("image");

                            for (int i = 0; i < ary_imgs.length(); i++) {

                                JSONObject objimg = ary_imgs.getJSONObject(i);

                                Images_List imglistdata = new Images_List();

                                imglistdata.setImage(objimg.getString("image"));

                                imagelist.add(imglistdata);


                            }
                            mViewPager.setAdapter(mCustomPagerAdapter);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvPostActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);
                map.put("store_id",store_id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
