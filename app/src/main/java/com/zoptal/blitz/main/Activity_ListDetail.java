package com.zoptal.blitz.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.fragment.Fragment_LocMap;
import com.zoptal.blitz.model.Images_List;
import com.zoptal.blitz.model.PreviousAds_List;
import com.zoptal.blitz.model.Rating_list;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Activity_ListDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_storename, tv_saledescription, txt_remaining, tv_time, tv_desc, tv_address, tv_web,tv_spweb, tv_phn, tv_st, tv_end, tv_discount,tv_open;
    TextView tv_ratingmsg, tv_rpreadsgmsg;
    private ImageView img_backarrow, img_fb, img_twitter, img_chat, img_fav, img_unfav;
    private  RelativeLayout rel_web,rel_spclweb;
    private ImageView img_star1, img_star2, img_star3, img_star4, img_star5;
    private EditText ed_comment;
    private Button btn_submit;
    private Button btn_review;
    private RelativeLayout btn_discountcode;
    Custom_pageradapterimages mCustomPagerAdapter;
    CustomList_prevoiusads adapter_previousads;
    CustomList_ratinglist adapter_rating;
    ViewPager mViewPager;
    Dialog dialog;
    ListView listview, listview_rating;
    public final String MyPREFERENCES = "MyPrefs1";
    SharedPreferences sharedpreferences1;
    String buyer_access_token;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    ProgressDialog loading;
    public long milisecnds;
    private List<Images_List> imagelist = new ArrayList<Images_List>();
    private List<PreviousAds_List> preadslist = new ArrayList<PreviousAds_List>();
    private List<Rating_list> ratelist = new ArrayList<Rating_list>();
    String applink, storeid, usr_rating;
    int counter = 0;
    String ratingvalue;
    String storelat,storelng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listdetail);


        sharedpreferences1 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

        initview();
    }


    public void initview() {

        Log.e("id---", "" + Fragment_LocMap.ad_id);

        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);

        tv_storename = (TextView) findViewById(R.id.tv_storename);
        tv_open=(TextView) findViewById(R.id.tv_open);
        tv_ratingmsg = (TextView) findViewById(R.id.tv_ratingmsg);
        tv_rpreadsgmsg = (TextView) findViewById(R.id.tv_rpreadsgmsg);
        tv_saledescription = (TextView) findViewById(R.id.tv_saledescription);
        txt_remaining = (TextView) findViewById(R.id.txt_remaining);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_web = (TextView) findViewById(R.id.tv_web);
        tv_spweb=(TextView) findViewById(R.id.tv_spweb);
        tv_phn = (TextView) findViewById(R.id.tv_phn);
        tv_st = (TextView) findViewById(R.id.tv_st);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_discount = (TextView) findViewById(R.id.tv_discount);

        rel_web=(RelativeLayout)findViewById(R.id.rel_web);
        rel_spclweb=(RelativeLayout)findViewById(R.id.rel_spclweb);

        mCustomPagerAdapter = new Custom_pageradapterimages(Activity_ListDetail.this, imagelist);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        img_fb = (ImageView) findViewById(R.id.img_fb);
        img_twitter = (ImageView) findViewById(R.id.img_twitter);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_fav = (ImageView) findViewById(R.id.img_fav);
        img_unfav = (ImageView) findViewById(R.id.img_unfav);
        btn_review = (Button) findViewById(R.id.btn_review);
        btn_discountcode = (RelativeLayout) findViewById(R.id.btn_discountcode);
        listview = (ListView) findViewById(R.id.listview);
        listview_rating = (ListView) findViewById(R.id.listview_rating);
        listview.setFocusable(false);
        listview_rating.setFocusable(false);
        adapter_previousads = new CustomList_prevoiusads(Activity_ListDetail.this, preadslist);

        adapter_rating = new CustomList_ratinglist(Activity_ListDetail.this, ratelist);

        listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listview_rating.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        img_backarrow.setOnClickListener(this);
        img_fb.setOnClickListener(this);
        img_twitter.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        img_fav.setOnClickListener(this);
        img_unfav.setOnClickListener(this);
        btn_discountcode.setOnClickListener(this);
        tv_phn.setOnClickListener(this);
        tv_web.setOnClickListener(this);
        tv_spweb.setOnClickListener(this);
        tv_address.setOnClickListener(this);

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Activity_ListDetail.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Activity_ListDetail.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }

        getfeeddetail();

        applink();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:
                finish();
             //   checkRunTimePermission();


//                if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
//                            11);
//
//                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                    // app-defined int constant
//
//                    return;
//                }
//             //   finish();
//                Intent callIntent1 = new Intent(Intent.ACTION_CALL);
//                callIntent1.setData(Uri.parse("tel:0377778888"));
//
//                if (ActivityCompat.checkSelfPermission(Activity_ListDetail.this,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                startActivity(callIntent1);

                break;

            case R.id.img_unfav:

                if (buyer_access_token.isEmpty()) {

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(Activity_ListDetail.this, "Please Login to use this feature.");

                    alertmsg.dialog.show();
                } else {

                    remove_fav();
                }

                // finish();

                break;


            case R.id.img_fav:

                if (buyer_access_token.isEmpty()) {

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(Activity_ListDetail.this, "Please Login to use this feature.");

                    alertmsg.dialog.show();
                } else {

                    add_fav();
                }

                break;
            case R.id.tv_web:
                String weburl=tv_web.getText().toString().trim();
                if(weburl.contains("http://")){

                }else{
                    weburl="http://"+tv_web.getText().toString().trim();
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(weburl));
                startActivity(browserIntent);

                break;

            case R.id.tv_spweb:

                String weburlspcl=tv_spweb.getText().toString().trim();

                if(weburlspcl.contains("http://")){

                }else{
                    weburlspcl="http://"+tv_spweb.getText().toString().trim();
                }

                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(weburlspcl));
                startActivity(browserIntent1);

                break;



            case R.id.tv_address:

                Uri gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH,"geo:%f,%f", Double.parseDouble(storelat),Double.parseDouble(storelat)));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

                break;
            case R.id.tv_phn:
                Log.e("phn nummm", "" + tv_phn.getText().toString().trim());

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ tv_phn.getText().toString().trim()));
                startActivity(intent);

//                if (ActivityCompat.checkSelfPermission(Activity_ListDetail.this,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                startActivity(callIntent);

                break;

            case R.id.img_fb:

                shareonFacebook();

                break;

            case R.id.img_twitter:

                shareonTwitter();

                break;


            case R.id.img_chat:

                Uri uri = Uri.parse("smsto:" + "");

                Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);

                // add the message at the sms_body extra field
                smsSIntent.putExtra("sms_body",applink);

      //  smsSIntent.putExtra("sms_body", "Download and follow me on the City Local Life app! Here are the links :-\niOS :-https://itunes.apple.com/us/app/city-local-life/id986724674?ls=1&mt=8\nAndroid :-https://play.google.com/store/apps/details?id=com.mindbowser.citylocallife&hl=en");

                try{

                    startActivity(smsSIntent);

                } catch (Exception ex) {

                    Toast.makeText(Activity_ListDetail.this, "Your sms has failed...",

                            Toast.LENGTH_LONG).show();

                    ex.printStackTrace();

                }


//                sendSMSMessage();

//                SmsManager smsManager =     SmsManager.getDefault();
//                smsManager.sendTextMessage("9780801992", null,applink, null, null);

                break;

            case R.id.btn_review:

                if(buyer_access_token.isEmpty()){

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(Activity_ListDetail.this, "Please Login to use this feature.");

                    alertmsg.dialog.show();
                    return;
                }
                else if(usr_rating.equals("1")) {

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(Activity_ListDetail.this, "You already gives rating to this store");

                    alertmsg.dialog.show();
                    return;
                }
                else {
                    dialogreview();
                }

                break;

            case R.id.btn_discountcode:

                if(buyer_access_token.isEmpty()){

                    AlertDialogMsg alertmsg = new
                            AlertDialogMsg(Activity_ListDetail.this, "Please Login to use this feature.");

                    alertmsg.dialog.show();
                }
                else{

                    discount_code();
                }

                break;

        }

    }

    private void shareonFacebook() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setPackage("com.facebook.katana");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,applink);
        try {
            startActivity(sharingIntent);

        }
        catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(Activity_ListDetail.this,"Facebook have not been installed on this device",Toast.LENGTH_LONG).show();
        }
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11);
        } else {

            Intent callIntent1 = new Intent(Intent.ACTION_CALL);
            callIntent1.setData(Uri.parse("tel:0377778888"));

            if (ActivityCompat.checkSelfPermission(Activity_ListDetail.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent1);

            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }
    private void shareonTwitter() {

        try
        {
            // Check if the Twitter app is installed on the phone.
            getPackageManager().getPackageInfo("com.twitter.android", 0);

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
            //  intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,applink);


//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("image/*");
//
         //   sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,title);

         //   sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }
        catch (Exception e)
        {
            Toast.makeText(Activity_ListDetail.this,"Twitter is not installed on this device",Toast.LENGTH_LONG).show();

        }
    }


    public void dialogreview() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Activity_ListDetail.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addreview);


        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        img_star1= (ImageView) dialog.findViewById(R.id.img_star1);
        img_star2= (ImageView) dialog.findViewById(R.id.img_star2);
        img_star3= (ImageView) dialog.findViewById(R.id.img_star3);
        img_star4= (ImageView) dialog.findViewById(R.id.img_star4);
        img_star5= (ImageView) dialog.findViewById(R.id.img_star5);
        ed_comment=(EditText)dialog.findViewById(R.id.ed_comment);
        btn_submit=(Button)dialog.findViewById(R.id.btn_submit);

        img_star1.setTag(R.mipmap.disable);
        img_star2.setTag(R.mipmap.disable);
        img_star3.setTag(R.mipmap.disable);
        img_star4.setTag(R.mipmap.disable);
        img_star5.setTag(R.mipmap.disable);


        img_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int resId =(Integer) view.getTag();

                if(resId == R.mipmap.disable) {
                    ratingvalue="0.5";
                    img_star1.setBackgroundResource(R.mipmap.half);
                    img_star1.setTag(R.mipmap.half);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star4.setBackgroundResource(R.mipmap.disable);
                    img_star3.setBackgroundResource(R.mipmap.disable);
                    img_star2.setBackgroundResource(R.mipmap.disable);
                    // background is R.drawable.williboese image
                }
                 if(resId==R.mipmap.half){
                     ratingvalue="1";
                    img_star1.setBackgroundResource(R.mipmap.star);
                     img_star1.setTag(R.mipmap.star);
                     img_star5.setBackgroundResource(R.mipmap.disable);
                     img_star4.setBackgroundResource(R.mipmap.disable);
                     img_star3.setBackgroundResource(R.mipmap.disable);
                     img_star2.setBackgroundResource(R.mipmap.disable);
                }
               if(resId==R.mipmap.star){
                   ratingvalue="0";
                   img_star1.setBackgroundResource(R.mipmap.disable);
                   img_star1.setTag(R.mipmap.disable);
               }
//                img_star1.setBackgroundResource(R.mipmap.half);
//                counter=counter+1;
//                img_star1.setClickable(false);
//                Log.e("counter"," "+counter);
            }
        });

        img_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_star1.setBackgroundResource(R.mipmap.star);
                int resId =(Integer) view.getTag();

                if(resId == R.mipmap.disable) {
                    ratingvalue="1.5";
                    img_star2.setBackgroundResource(R.mipmap.half);
                    img_star2.setTag(R.mipmap.half);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star4.setBackgroundResource(R.mipmap.disable);
                    img_star3.setBackgroundResource(R.mipmap.disable);

                    // background is R.drawable.williboese image
                }
                if(resId==R.mipmap.half){
                    ratingvalue="2";
                    img_star2.setBackgroundResource(R.mipmap.star);
                    img_star2.setTag(R.mipmap.star);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star4.setBackgroundResource(R.mipmap.disable);
                    img_star3.setBackgroundResource(R.mipmap.disable);
                }
                if(resId==R.mipmap.star){
                    ratingvalue="1";
                    img_star2.setBackgroundResource(R.mipmap.disable);
                    img_star2.setTag(R.mipmap.disable);
                }

//                img_star2.setBackgroundResource(R.mipmap.star);
//                counter=counter+1;
//                Log.e("counter"," "+counter);
//                img_star2.setClickable(false);
            }
        });
        img_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_star1.setBackgroundResource(R.mipmap.star);
                img_star2.setBackgroundResource(R.mipmap.star);
                int resId =(Integer) view.getTag();

                if(resId == R.mipmap.disable) {
                    ratingvalue="2.5";
                    img_star3.setBackgroundResource(R.mipmap.half);
                    img_star3.setTag(R.mipmap.half);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star4.setBackgroundResource(R.mipmap.disable);

                    // background is R.drawable.williboese image
                }
                if(resId==R.mipmap.half){
                    ratingvalue="3";
                    img_star3.setBackgroundResource(R.mipmap.star);
                    img_star3.setTag(R.mipmap.star);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star4.setBackgroundResource(R.mipmap.disable);
                }
                if(resId==R.mipmap.star){
                    ratingvalue="2";
                    img_star3.setBackgroundResource(R.mipmap.disable);
                    img_star3.setTag(R.mipmap.disable);
                }

//                img_star3.setBackgroundResource(R.mipmap.star);
//                counter=counter+1;
//                Log.e("counter"," "+counter);
//                img_star3.setClickable(false);
            }
        });
        img_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_star1.setBackgroundResource(R.mipmap.star);
                img_star2.setBackgroundResource(R.mipmap.star);
                img_star3.setBackgroundResource(R.mipmap.star);
                int resId =(Integer) view.getTag();

                if(resId == R.mipmap.disable) {
                    ratingvalue="3.5";
                    img_star4.setBackgroundResource(R.mipmap.half);
                    img_star4.setTag(R.mipmap.half);
                    img_star5.setBackgroundResource(R.mipmap.disable);

                    // background is R.drawable.williboese image
                }
                if(resId==R.mipmap.half){
                    ratingvalue="4";
                    img_star4.setBackgroundResource(R.mipmap.star);
                    img_star4.setTag(R.mipmap.star);
                    img_star5.setBackgroundResource(R.mipmap.disable);
                }
                if(resId==R.mipmap.star){
                    ratingvalue="3";
                    img_star4.setBackgroundResource(R.mipmap.disable);
                    img_star4.setTag(R.mipmap.disable);
                }

//                img_star4.setBackgroundResource(R.mipmap.star);
//                counter=counter+1;
//                img_star4.setClickable(false);
//                Log.e("counter"," "+counter);
            }
        });
        img_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_star1.setBackgroundResource(R.mipmap.star);
                img_star2.setBackgroundResource(R.mipmap.star);
                img_star3.setBackgroundResource(R.mipmap.star);
                img_star4.setBackgroundResource(R.mipmap.star);
                int resId =(Integer) view.getTag();

                if(resId == R.mipmap.disable) {
                    ratingvalue="4.5";
                    img_star5.setBackgroundResource(R.mipmap.half);
                    img_star5.setTag(R.mipmap.half);
                    // background is R.drawable.williboese image
                }
                if(resId==R.mipmap.half){
                    ratingvalue="5";
                    img_star5.setBackgroundResource(R.mipmap.star);
                    img_star5.setTag(R.mipmap.star);
                }
                if(resId==R.mipmap.star){
                    ratingvalue="4";
                    img_star5.setBackgroundResource(R.mipmap.disable);
                    img_star5.setTag(R.mipmap.disable);
                }

//                img_star5.setBackgroundResource(R.mipmap.star);
//                counter=counter+1;
//                img_star5.setClickable(false);
//                Log.e("counter"," "+counter);
            }
        });


        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("rating value===",""+ratingvalue);

               review_storerating();

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void getfeeddetail() {

        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.home_feeds_deatil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                            storeid=obj.getString("store_id");
                            String storename  =obj.getString("store_name");
                            String sale_desc  =obj.getString("sales_description");
                            String remainingtime =obj.getString("remaing_time_ss");
                            String starttime=obj.getString("starting_in_ss");
                            String desc =obj.getString("description");
                            String storeaddress =obj.getString("store_address");
                            storelat=obj.getString("store_latitude");
                            storelng=obj.getString("store_longitude");
                            String cmpnywebsite =obj.getString("company_website");
                            String special_website=obj.getString("special_website");
                            String storephn =obj.getString("store_phone");
                            String hourfrm =obj.getString("store_hours_from");
                            String hourto =obj.getString("store_hours_to");
                            String storeopen =obj.getString("store_open");

                    if(cmpnywebsite.isEmpty()){
                        rel_web.setVisibility(View.GONE);
                    }
                      if(special_website.isEmpty()){
                          rel_spclweb.setVisibility(View.GONE);

                      }

                            if(storeopen.equals("0")){
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

                          JSONArray ary_previousads= main_obj.getJSONArray("previou_ads");

                            for (int i = 0; i < ary_previousads.length(); i++) {

                                JSONObject objads = ary_previousads.getJSONObject(i);

                                PreviousAds_List adslistdata = new PreviousAds_List();

                                adslistdata.setAd_id(objads.getString("ad_id"));
                                adslistdata.setCategory(objads.getString("category"));
                                adslistdata.setSales_heading(objads.getString("sales_heading"));
                                adslistdata.setSales_description(objads.getString("sales_description"));


                                preadslist.add(adslistdata);

                            }

                    JSONArray ary_rate= main_obj.getJSONArray("rating");

                            for (int i = 0; i < ary_rate.length(); i++) {

                                JSONObject objrt = ary_rate.getJSONObject(i);

                                Rating_list ratelistdata = new Rating_list();

                                ratelistdata.setRating(objrt.getString("rating"));
                                ratelistdata.setComment(objrt.getString("comment"));
                                ratelistdata.setRating_by(objrt.getString("rating_by"));

                                ratelist.add(ratelistdata);

                            }

                                String fav=main_obj.getString("favorites");
                                usr_rating=main_obj.getString("user_rating");

                            if(fav.equals("0")){
                               img_fav.setVisibility(View.VISIBLE);
                                img_unfav.setVisibility(View.GONE);
                            }
                            else{
                                img_unfav.setVisibility(View.VISIBLE);
                                img_fav.setVisibility(View.GONE);
                            }

                            Log.e("preadslist size--",""+preadslist.size());

                            mViewPager.setAdapter(mCustomPagerAdapter);
                            listview.setAdapter(adapter_previousads);
                            listview_rating.setAdapter(adapter_rating);

                            tv_storename.setText(Fragment_LocMap.cmpny_name);
                            tv_saledescription.setText(sale_desc);
                         //   tv_time.setText(remainingtime);
                            tv_desc.setText(desc);
                            tv_address.setText(storeaddress);
                            tv_web.setText(cmpnywebsite);
                            tv_spweb.setText(special_website);
                            tv_phn.setText(storephn);
                            tv_st.setText(hourfrm +" "+"-");
                            tv_end.setText(hourto);



                            if(starttime.equals("0")) {

                                txt_remaining.setText("Remaining");
                                int remainingtym = Integer.parseInt(remainingtime);
                                calculateTime(remainingtym);
                            }
                            else {
                                txt_remaining.setText("Starting in");
                                int startingtym = Integer.parseInt(starttime);
                                calculateTime(startingtym);

                            }

                            new CountDownTimer(milisecnds, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    long secondsInMilli = 1000;
                                    long minutesInMilli = secondsInMilli * 60;
                                    long hoursInMilli = minutesInMilli * 60;

                                    long elapsedHours = millisUntilFinished / hoursInMilli;
                                    millisUntilFinished = millisUntilFinished % hoursInMilli;

                                    long elapsedMinutes = millisUntilFinished / minutesInMilli;
                                    millisUntilFinished = millisUntilFinished % minutesInMilli;

                                    long elapsedSeconds = millisUntilFinished / secondsInMilli;

                                    String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes,elapsedSeconds);
                                    tv_time.setText(yy);
                                }



                                public void onFinish() {

                                    tv_time.setText("00:00:00");
                                }
                            }.start();




                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("ad_id", Fragment_LocMap.ad_id);
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public  void calculateTime(long seconds) {
//         int day = (int) TimeUnit.SECONDS.toDays(seconds);
//        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
//        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
//        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
//        long miliseconds = TimeUnit.SECONDS.toMillis(seconds) - (TimeUnit.SECONDS.toSeconds(seconds) *60);

        milisecnds=seconds*1000;
        //  Log.e(" tymmmmm","Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second +"Miliseconds "+  miliseconds);

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

Log.e("pavkkagr itme...",""+packageItems.size());
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
                Picasso.with(Activity_ListDetail.this).load(packageItems.get(position).getImage()).resize(800, 500).into(imageView);
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


    public class CustomList_prevoiusads extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        private List<PreviousAds_List> adprevious;

        public CustomList_prevoiusads(Context context, List<PreviousAds_List> adprevious) {

            this.context = context;
            this.adprevious = adprevious;
               inflater = LayoutInflater.from(context);

            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            //   Log.e("homefeedsize",""+homefeed.size());
            if(adprevious.size()==0){
                tv_rpreadsgmsg.setVisibility(View.VISIBLE);

            }
            if(adprevious.size()>0){
                tv_rpreadsgmsg.setVisibility(View.GONE);

            }

            return adprevious.size();

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

            final Holdlerpreads holder = new Holdlerpreads();
            convertView = inflater.inflate(R.layout.customview_previousads, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_headingname = (TextView) convertView.findViewById(R.id.tv_headingname);
            holder.tv_colr = (TextView) convertView.findViewById(R.id.tv_colr);
            holder.relative_list = (RelativeLayout) convertView.findViewById(R.id.relative_list);

            holder.tv_name.setText(adprevious.get(position).getSales_description());
          //  holder.tv_headingname.setText(adprevious.get(position).getSales_heading());
            //   holder.tv_colr.setBackgroundColor(Color.parseColor(homefeed.get(position).getColor_code()));

            if(adprevious.get(position).getCategory().equals("Restaurant")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#e09e6d"));
            }
            if(adprevious.get(position).getCategory().equals("Bars/Clubs")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#dfaad1"));
            }

            if(adprevious.get(position).getCategory().equals("Retail")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));
            }
            if(adprevious.get(position).getCategory().equals("Things To Do")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#9ec780"));
            }
            if(adprevious.get(position).getCategory().equals("Tickets&Events")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
            }


            holder.relative_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            adapter_previousads.notifyDataSetChanged();
            return convertView;

        }

        class Holdlerpreads {
            TextView tv_name;
            TextView tv_headingname;
            TextView tv_colr;
            RelativeLayout relative_list;
        }
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
            if(ratinglist.size()==0){
                tv_ratingmsg.setVisibility(View.VISIBLE);

            }
            else{
                tv_ratingmsg.setVisibility(View.GONE);

            }
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
            convertView = inflater.inflate(R.layout.customview_ratinglist, null);

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
    private void applink() {

//        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
//        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.get_app_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  loading.dismiss();

                        Log.e("respose app link===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                          applink =obj.getString("app_link_android");

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();


                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void add_fav() {

        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_favourite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      loading.dismiss();

                        Log.e("respose add fav..===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code=main_obj.getString("code");
                            String msg=main_obj.getString("message");

                    if(code.equals("200")){

                        img_unfav.setVisibility(View.VISIBLE);
                        img_fav.setVisibility(View.GONE);

                        AlertDialogMsg alertmsg = new
                                AlertDialogMsg(Activity_ListDetail.this,msg);

                        alertmsg.dialog.show();
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
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("store_id",storeid);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void remove_fav() {

        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_favourite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose add fav===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code=main_obj.getString("code");
                            String msg=main_obj.getString("message");

                            if(code.equals("200")){

                                img_unfav.setVisibility(View.GONE);
                                img_fav.setVisibility(View.VISIBLE);

                                AlertDialogMsg alertmsg = new
                                        AlertDialogMsg(Activity_ListDetail.this,msg);

                                alertmsg.dialog.show();
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
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("store_id",storeid);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void discount_code() {

        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.discount_code,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose discount code===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            Boolean respons=main_obj.getBoolean("response");


                            Log.e("Boolean respons===",""+respons);

                            if(respons.equals(false)) {

                                String msg=main_obj.getString("message");

                                AlertDialogMsg alertmsg = new
                                        AlertDialogMsg(Activity_ListDetail.this, msg);

                                alertmsg.dialog.show();
                            }
                            String code=main_obj.getString("discount_code");

                            tv_discount.setText(code);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("ad_id",Fragment_LocMap.ad_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void review_storerating() {

        loading = new ProgressDialog(Activity_ListDetail.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        final String comment=ed_comment.getText().toString().trim();

        if (comment.isEmpty()){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Activity_ListDetail.this, "Please Provide Comment.");

            alertmsg.dialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.store_rating,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose review===",""+response);


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg=main_obj.getString("message");

                            Toast.makeText(Activity_ListDetail.this,msg,Toast.LENGTH_LONG ).show();

                            getfeeddetail();
                            adapter_rating.notifyDataSetChanged();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_ListDetail.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();


                map.put("access_token",buyer_access_token);
                map.put("store_id",storeid);
                map.put("rating",ratingvalue);
                map.put("comment",comment);

                Log.e("ratttt",""+ratingvalue);
                Log.e("map====",""+map);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                      0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("phone number", null, applink, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            }

//            case 11: {
////                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
////                startActivity(intent);
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e("pre","ok");
//                    Intent callIntent1 = new Intent(Intent.ACTION_DIAL);
//                    callIntent1.setData(Uri.parse("0377778888"));
//
//                    if (ActivityCompat.checkSelfPermission(Activity_ListDetail.this,
//                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    startActivity(callIntent1);
//                    // permission was granted, yay! do the
//                    // calendar task you need to do.
//
//                } else {
//Log.e("no pre","ok");
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ListDetail.this);
//                    builder.setTitle("Need Permission");
//                    builder.setMessage("This app needs phone permission.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},11);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    builder.show();
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//
//            }
//


        }

    }

}
