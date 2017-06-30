package com.zoptal.blitz.fragment;


import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.main.Activity_ListDetail;
import com.zoptal.blitz.main.MainActivity1;
import com.zoptal.blitz.main.MyAdapter;
import com.zoptal.blitz.model.HomeFeeds_list;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_LocMap extends Fragment {

    MainActivity1 activity1;
    public ListView listview;
    public TextView tv_msg;

    View view;

    public final String MyPREFERENCES = "MyPrefs1";
    SharedPreferences sharedpreferences1;
    String buyer_access_token;

    ProgressDialog loading;
    double latitude, longitude;
    GPSTracker gps;

    Context mContext;
    double lat = 0.00, lng = 0.00;
    CustomList_ads adapter_ads;

    private List<HomeFeeds_list> homefeedlistList = new ArrayList<HomeFeeds_list>();

    String cmpanycategory;
    public static String ad_id;
    String srchvalue = "";
    String cmpnyname = "";

    public static String cmpny_name="";
    String cmpnyhead = "";
    MapFragment mapFragment;
    int  pos;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment__locate_us, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activity1 = (MainActivity1) getActivity();
        activity1.img_srch.setVisibility(View.VISIBLE);
        activity1.img_star.setVisibility(View.GONE);
        activity1.img_cross.setVisibility(View.GONE);
        activity1.ed_srch.setVisibility(View.GONE);
        activity1.img_dltall.setVisibility(View.GONE);


        sharedpreferences1 = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

        mContext = getActivity();

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                gps.showSettingsAlert();
            }
        }
        initMap();

        return view;

    }

    private void initMap() {

        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        // MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map1);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

               BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinpink);


                LatLng latLng = new LatLng(latitude, longitude);
             //   googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
              googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
               googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//
//                googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(null));
////                            LatLng loc = new LatLng(latitude, longitude);
////                            Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
////                            if(googleMap != null){
////                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
////                                googleMap.addMarker(new MarkerOptions().position(loc).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
////                            }
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
              //  googleMap.setMyLocationEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        listview = (ListView) view.findViewById(R.id.listview);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);



        activity1.ed_srch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (MyAdapter.itemvalue == 0) {

                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);

                        cmpanycategory = "All";
                        srchvalue = activity1.ed_srch.getText().toString();
                        get_feeds();


                    }

                    if (MyAdapter.itemvalue == 1) {

                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);
                        cmpanycategory = "Restaurant";
                        srchvalue = activity1.ed_srch.getText().toString();
                        get_feeds();

                    }
                    if (MyAdapter.itemvalue == 2) {

                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);
                        cmpanycategory = "Bars/Clubs";

                        srchvalue = activity1.ed_srch.getText().toString();

                        get_feeds();
                    }

                    if (MyAdapter.itemvalue == 3) {

                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);
                        cmpanycategory = "Retail";

                        srchvalue = activity1.ed_srch.getText().toString();
                        get_feeds();

                    }

                    if (MyAdapter.itemvalue == 4) {
                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);
                        cmpanycategory = "Things To do";

                        srchvalue = activity1.ed_srch.getText().toString();
                        get_feeds();
                    }

                    if (MyAdapter.itemvalue == 5) {
                        homefeedlistList.clear();
                        adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
                        listview.setAdapter(adapter_ads);
                        cmpanycategory = "Tickets&Events";

                        srchvalue = activity1.ed_srch.getText().toString();

                        get_feeds();
                    }

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                    return true;
                }
                return false;
            }

        });


        if (MyAdapter.itemvalue == 0) {

            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);

            cmpanycategory = "All";

            get_feeds();

        }

        if (MyAdapter.itemvalue == 1) {
            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);
            cmpanycategory = "Restaurant";

            get_feeds();

        }

        if (MyAdapter.itemvalue == 2) {
            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);
            cmpanycategory = "Bars/Clubs";

            get_feeds();
        }

        if (MyAdapter.itemvalue == 3) {
            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);
            cmpanycategory = "Retail";

            get_feeds();

        }
        if (MyAdapter.itemvalue == 4) {
            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);
            cmpanycategory = "Things To do";

            get_feeds();
        }
        if (MyAdapter.itemvalue == 5) {
            adapter_ads = new CustomList_ads(getActivity(), homefeedlistList);
            listview.setAdapter(adapter_ads);
            cmpanycategory = "Tickets&Events";

            get_feeds();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map1);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    private void get_feeds() {

        loading = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.home_feeds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store = main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                HomeFeeds_list homefeedlistdata = new HomeFeeds_list();

                                homefeedlistdata.setId(obj.getString("id"));
                                homefeedlistdata.setCompany_category(obj.getString("company_category"));
                                //   homefeedlistdata.setColor_code(obj.getString("color_code"));
                                homefeedlistdata.setSales_description(obj.getString("sales_description"));
                                homefeedlistdata.setSales_heading(obj.getString("sales_heading"));
                                homefeedlistdata.setStarting_in_ss(obj.getString("starting_in_ss"));
                                homefeedlistdata.setStart_date(obj.getString("start_date"));
                                homefeedlistdata.setStart_time_hr(obj.getString("start_time_hr"));
                                homefeedlistdata.setStart_time_min(obj.getString("start_time_min"));
                                homefeedlistdata.setEnd_time_hr(obj.getString("end_time_hr"));
                                homefeedlistdata.setEnd_time_min(obj.getString("end_time_min"));
                                homefeedlistdata.setCurrent_time(obj.getString("current_time"));
                                homefeedlistdata.setExpire_time(obj.getString("expire_time"));
                                homefeedlistdata.setRemaing_time_ss(obj.getString("remaing_time_ss"));
                                homefeedlistdata.setStore_id(obj.getString("store_id"));
                                homefeedlistdata.setOld_price(obj.getString("old_price"));
                                homefeedlistdata.setNew_price(obj.getString("new_price"));
                                homefeedlistdata.setDiscount_per(obj.getString("discount_per"));
                                homefeedlistdata.setStore_name(obj.getString("store_name"));
                                homefeedlistdata.setStore_address(obj.getString("store_address"));
                                homefeedlistdata.setStore_latitude(obj.getString("store_latitude").toString().trim());
                                homefeedlistdata.setStore_longitude(obj.getString("store_longitude").toString().trim());
                                homefeedlistdata.setBusiness_name(obj.getString("business_name"));
                                homefeedlistdata.setDescription(obj.getString("description"));
                                homefeedlistdata.setStore_fav(obj.getString("store_fav"));
                                homefeedlistdata.setVisitor(obj.getString("visitor"));
                                homefeedlistdata.setStatus(obj.getString("status"));
                                homefeedlistdata.setPosted(obj.getString("posted"));

                                Log.e("respose===", "" + response);
// adding storelistdata to storeList array
                                homefeedlistList.add(homefeedlistdata);

                             //   Log.e("homelistfess", "" + homefeedlistList.size());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter_ads.notifyDataSetChanged();

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //    Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("access_token", buyer_access_token);
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                map.put("search", srchvalue);
                map.put("company_category", cmpanycategory);
                map.put("page_no", "1");
                map.put("no_of_post", "100");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public class CustomList_ads extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        private List<HomeFeeds_list> homefeed;
        //  String [] name;
        //String [] headingname;
        public long milisecnds;

        public CustomList_ads(Context context, List<HomeFeeds_list> homefeed) {

            this.context = context;
            this.homefeed = homefeed;
            //   this.headingname=headingname;
            inflater = LayoutInflater.from(context);

            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            //   Log.e("homefeedsize",""+homefeed.size());
            if (homefeed.size() == 0) {
                tv_msg.setVisibility(View.VISIBLE);

            } else {
                tv_msg.setVisibility(View.GONE);

            }
            return homefeed.size();

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

            final Holdleroffer holder = new Holdleroffer();
            convertView = inflater.inflate(R.layout.customview_offers, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_headingname = (TextView) convertView.findViewById(R.id.tv_headingname);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_remaining = (TextView) convertView.findViewById(R.id.tv_remaining);
            holder.tv_colr = (TextView) convertView.findViewById(R.id.tv_colr);
            holder.relative_list = (RelativeLayout) convertView.findViewById(R.id.relative_list);


            if (homefeed.size() > 0) {
                try {

              mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap map) {




                            if (MyAdapter.itemvalue == 0) {

                                LatLng ll = null;


                                for (int i = 0; i < homefeed.size(); i++) {

                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)

                                    try {
                                        double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                        double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                        ll = new LatLng(lt,lng);

                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }

//                                    Log.e("ad_id-----",""+homefeed.get(i).getid());
//                                    Log.e("ad_id----",""+ad_id);


                                    if(homefeed.get(i).getCompany_category().equals("Restaurant")) {

                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead=homefeed.get(i).getSales_heading();



                                        if(homefeed.get(i).getStore_fav().equals("1")){


                                          //  Log.e("mrkr pos-----",""+pos);

                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                 Marker marker =map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                        }
                                        else {
                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinbrown);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                            Marker marker =map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);


                                        }


//
                                    }
                                    if(homefeed.get(i).getCompany_category().equals("Bars/Clubs")) {
                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead = homefeed.get(i).getSales_heading();


                                        if (homefeed.get(i).getStore_fav().equals("1")) {


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                        Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                        }
                                        else {


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinpink);
                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                           Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);
                                        }
                                    }

                                    if(homefeed.get(i).getCompany_category().equals("Retail")) {
                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead = homefeed.get(i).getSales_heading();


                                        if (homefeed.get(i).getStore_fav().equals("1")) {


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                           Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                        } else {


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinred);
                                        //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                      Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                    }
                                    }

                                    if(homefeed.get(i).getCompany_category().equals("Things To Do")) {
                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead = homefeed.get(i).getSales_heading();


                                        if (homefeed.get(i).getStore_fav().equals("1")) {


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                           Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                        }
                                        else {

                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpingreen);
                                        //  LatLng latLng = new LatLng(lat, lng);


                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                       Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);
                                    }
                                    }


                                    if(homefeed.get(i).getCompany_category().equals("Tickets&Events")) {
                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead = homefeed.get(i).getSales_heading();

                                        Log.e("h stor fav",""+homefeed.get(i).getStore_fav());

                                        if(homefeed.get(i).getStore_fav().equals("1")){


                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);

                                            //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                          Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);

                                        }
                                        else {

                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinblue);
                                        //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));
                                       Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);
                                    }
                                    }

                                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            int pos = mHashMap.get(marker);
                                            Log.e("Position of arraylist 0 cater", pos+"");

                                            //     int id = Integer.parseInt(marker.getId());
                                            Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                            ad_id = homefeed.get(pos).getid();
                                            cmpny_name = homefeed.get(pos).getBusiness_name();

                                            Intent i = new Intent(context, Activity_ListDetail.class);
                                            context.startActivity(i);
                                        }
                                    });


                                }
                            }
                            if (MyAdapter.itemvalue == 1) {
                                LatLng ll=null;
                                for (int i = 0; i < homefeed.size(); i++) {

                                    cmpnyname = homefeed.get(i).getBusiness_name();
                                    cmpnyhead = homefeed.get(i).getSales_heading();


                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    try {
                                        double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                        double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                        ll = new LatLng(lt, lng);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    // LatLng ll = new LatLng(Double.parseDouble(homefeed.get(i).getStore_latitude()), Double.parseDouble(homefeed.get(i).getStore_longitude()));
                                    if (homefeed.get(i).getStore_fav().equals("1")) {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);  //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));
                                        Marker marker=map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);

                                    } else {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinbrown);      //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                    Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);




                                    }
                                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            int pos = mHashMap.get(marker);
                                            Log.e("Position of arraylist", pos+"");

                                       //     int id = Integer.parseInt(marker.getId());
                                            Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                            ad_id = homefeed.get(pos).getid();
                                            cmpny_name = homefeed.get(pos).getBusiness_name();

                                                Intent i = new Intent(context, Activity_ListDetail.class);
                                             context.startActivity(i);
                                        }
                                    });

                                }

                            }

                            if (MyAdapter.itemvalue == 2) {
//                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinpink);
//                                LatLng latLng = new LatLng(lat, lng);
//                                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                                map.addMarker(new MarkerOptions().position(latLng).title(cmpnyname).icon(icon));


                                LatLng ll=null;
                                for (int i = 0; i < homefeed.size(); i++) {
                                    cmpnyname = homefeed.get(i).getBusiness_name();
                                    cmpnyhead = homefeed.get(i).getSales_heading();

                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    try {
                                        double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                        double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                        ll = new LatLng(lt, lng);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    // LatLng ll = new LatLng(Double.parseDouble(homefeed.get(i).getStore_latitude()), Double.parseDouble(homefeed.get(i).getStore_longitude()));

                                    if (homefeed.get(i).getStore_fav().equals("1")) {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);  //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                     Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);

                                    } else {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinpink);    //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                      Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);
                                    }


                                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            int pos = mHashMap.get(marker);
                                            Log.e("Position of arraylist", pos+"");

                                            //     int id = Integer.parseInt(marker.getId());
                                            Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                            ad_id = homefeed.get(pos).getid();
                                            cmpny_name = homefeed.get(pos).getBusiness_name();

                                            Intent i = new Intent(context, Activity_ListDetail.class);
                                            context.startActivity(i);
                                        }
                                    });
                                }

                            }

                            if (MyAdapter.itemvalue == 3) {
//                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinred);
//                                LatLng latLng = new LatLng(lat, lng);
//                                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                                map.addMarker(new MarkerOptions().position(latLng).title(cmpnyname).icon(icon));

                                LatLng ll=null;
                                for (int i = 0; i < homefeed.size(); i++) {
                                    cmpnyname = homefeed.get(i).getBusiness_name();
                                    cmpnyhead=homefeed.get(i).getSales_heading();


                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    try {
                                        double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                        double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                        ll = new LatLng(lt,lng);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                   // LatLng ll = new LatLng(Double.parseDouble(homefeed.get(i).getStore_latitude()), Double.parseDouble(homefeed.get(i).getStore_longitude()));
                                    if (homefeed.get(i).getStore_fav().equals("1")) {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);  //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                       Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);

                                    } else {
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinred);   //  LatLng latLng = new LatLng(lat, lng);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                       Marker marker= map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                        mHashMap.put(marker, i);
                                    }

                                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            int pos = mHashMap.get(marker);
                                            Log.e("Position of arraylist", pos+"");

                                            //     int id = Integer.parseInt(marker.getId());
                                            Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                            ad_id = homefeed.get(pos).getid();
                                            cmpny_name = homefeed.get(pos).getBusiness_name();

                                            Intent i = new Intent(context, Activity_ListDetail.class);
                                            context.startActivity(i);
                                        }
                                    });
                                }

                            }

                            if (MyAdapter.itemvalue == 4) {
//                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpingreen);
//                                LatLng latLng = new LatLng(lat, lng);
//                                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                                map.addMarker(new MarkerOptions().position(latLng).title(cmpnyname).icon(icon));

                                LatLng ll=null;
                                for (int i = 0; i < homefeed.size(); i++) {
                                    cmpnyname = homefeed.get(i).getBusiness_name();
                                    cmpnyhead = homefeed.get(i).getSales_heading();

                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    try {
                                        double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                        double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                        ll = new LatLng(lt, lng);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                        if (homefeed.get(i).getStore_fav().equals("1")) {
                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);  //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                          Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));
                                            mHashMap.put(marker, i);

                                        } else {
                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpingreen);  //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));

                                          Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));

                                            mHashMap.put(marker, i);
                                        }

                                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            int pos = mHashMap.get(marker);
                                            Log.e("Position of arraylist", pos+"");

                                            //     int id = Integer.parseInt(marker.getId());
                                            Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                            ad_id = homefeed.get(pos).getid();
                                            cmpny_name = homefeed.get(pos).getBusiness_name();

                                            Intent i = new Intent(context, Activity_ListDetail.class);
                                            context.startActivity(i);
                                        }
                                    });
                                }

                            }

                            if (MyAdapter.itemvalue == 5) {
//                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow);
//                                LatLng latLng = new LatLng(lat, lng);
//                                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                                map.addMarker(new MarkerOptions().position(latLng).title(cmpnyname).icon(icon));
                                LatLng ll=null;

                                for (int i = 0; i < homefeed.size(); i++) {


                                        cmpnyname = homefeed.get(i).getBusiness_name();
                                        cmpnyhead = homefeed.get(i).getSales_heading();

                                        NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                        try {
                                            double lt = f.parse(homefeed.get(i).getStore_latitude()).doubleValue();
                                            double lng = f.parse(homefeed.get(i).getStore_longitude()).doubleValue();
                                            ll = new LatLng(lt, lng);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    if (homefeed.get(i).getCompany_category().equals("Tickets&Events")) {
                                        //   LatLng ll = new LatLng(Double.parseDouble(homefeed.get(i).getStore_latitude()), Double.parseDouble(homefeed.get(i).getStore_longitude()));

                                        Log.e("fav strioe listtt", " liiiiii" + homefeed.get(i).getStore_fav());
                                        if (homefeed.get(i).getStore_fav().equals("1")) {

                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinyellow); //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                       Marker marker=  map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));
                                            mHashMap.put(marker, i);


                                        } else {
                                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pointerpinblue); //  LatLng latLng = new LatLng(lat, lng);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,10));


                                         Marker marker=   map.addMarker(new MarkerOptions().position(ll).title(cmpnyname + "," + cmpnyhead).icon(icon));
                                            mHashMap.put(marker, i);
                                         //   holder.tv_colr.setBackgroundColor(Color.parseColor("#A9C5EB"));
                                        }

                                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            @Override
                                            public void onInfoWindowClick(Marker marker) {

                                                int pos = mHashMap.get(marker);
                                                Log.e("Position of arraylist", pos+"");

                                                //     int id = Integer.parseInt(marker.getId());
                                                Log.e("om mappp",position + "" +  homefeed.get(pos).getid());

                                                ad_id = homefeed.get(pos).getid();
                                                cmpny_name = homefeed.get(pos).getBusiness_name();

                                                Intent i = new Intent(context, Activity_ListDetail.class);
                                                context.startActivity(i);
                                            }
                                        });
                                    }
                                }

                            }


                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            map.setMyLocationEnabled(true);
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                       LatLng latLng = new LatLng(latitude, longitude);

                        // LatLng latLng = new LatLng(30.3398, 76.3869);

                         //   LatLng latLng = new LatLng(30.7333148, 76.7794179);



                            Log.e("on mapppp-----",""+latLng);
                            //   googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            //   googleMap.addMarker(new MarkerOptions().p
                            // osition(latLng).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                            map.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                            LatLng loc = new LatLng(latitude, longitude);
//                            Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
//                            if(googleMap != null){
//                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//                                googleMap.addMarker(new MarkerOptions().position(loc).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//                            }
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            //  googleMap.setMyLocationEnabled(false);
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        }
                    });
                } catch (NullPointerException e) {
                }

            }



            holder.tv_name.setText(homefeed.get(position).getBusiness_name());
            holder.tv_headingname.setText(homefeed.get(position).getSales_heading());
            //   holder.tv_colr.setBackgroundColor(Color.parseColor(homefeed.get(position).getColor_code()));


            String cmpny_category=homefeed.get(position).getCompany_category();


          // for (int i = 0; i < homefeed.size(); i++) {

                if(cmpny_category.equals("Retail")){

                    if(homefeed.get(position).getStore_fav().equals("1")){

                        holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
                    }
                    else {

                        holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));
                    }
                }

            if(cmpny_category.equals("Tickets&Events")){
             //   Log.e("Events","in ticket");
                if(homefeed.get(position).getStore_fav().equals("1")){
                    Log.e("Events","1");
                    holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
                }
                else {
                    Log.e("Events","0");
                  holder.tv_colr.setBackgroundColor(Color.parseColor("#A9C5EB"));
                }
            }
          //  }

                if(cmpny_category.equals("Restaurant")){

                    if(homefeed.get(position).getStore_fav().equals("1")){
                        holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
                    }
                    else {
                        holder.tv_colr.setBackgroundColor(Color.parseColor("#e09e6d"));
                    }
                }

                if(cmpny_category.equals("Bars/Clubs")){

                    if(homefeed.get(position).getStore_fav().equals("1")){
                        holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
                    }
                    else {

                        holder.tv_colr.setBackgroundColor(Color.parseColor("#dfaad1"));
                    }
                }


//                if(cmpny_category.equals("Retail")){
//
//                    if(homefeed.get(i).getStore_fav().equals("1")){
//                        holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
//                    }
//                    else {
//
//                        holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));
//                    }
//                }

                if(cmpny_category.equals("Things To Do")){
                    if(homefeed.get(position).getStore_fav().equals("1")){
                        holder.tv_colr.setBackgroundColor(Color.parseColor("#eeb840"));
                    }
                    else {
                        holder.tv_colr.setBackgroundColor(Color.parseColor("#9ec780"));
                    }
                }

//            if(cmpny_category.equals("Restaurant")){
//                holder.tv_colr.setBackgroundColor(Color.parseColor("#e09e6d"));
//            }

//            if(cmpny_category.equals("Bars/Clubs")){
//                holder.tv_colr.setBackgroundColor(Color.parseColor("#dfaad1"));
//            }

//            if(cmpny_category.equals("Retail")){
//                holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));
//            }
//            if(cmpny_category.equals("Things To Do")){
//                holder.tv_colr.setBackgroundColor(Color.parseColor("#9ec780"));
//            }
//            if(cmpny_category.equals("Tickets&Events")){
//
//                holder.tv_colr.setBackgroundColor(Color.parseColor("#A9C5EB"));
//            }


            NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
            try {
                lat = f.parse(homefeed.get(position).getStore_latitude()).doubleValue();
                lng= f.parse(homefeed.get(position).getStore_longitude()).doubleValue();


            } catch (ParseException e) {
                e.printStackTrace();
            }

//            lat= Double.parseDouble(homefeed.get(position).getStore_latitude());
//            lng=Double.parseDouble(homefeed.get(position).getStore_longitude());


            String strttime= homefeed.get(position).getStarting_in_ss();

            if(strttime.equals("0")) {

                holder.tv_remaining.setText("Remaining");
                int remainingtym = Integer.parseInt(homefeed.get(position).getRemaing_time_ss());
                calculateTime(remainingtym);
            }
            else {
                holder.tv_remaining.setText("Starting in");
                int startingtym = Integer.parseInt(homefeed.get(position).getStarting_in_ss());
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
                    holder.tv_time.setText(yy);
                }

                public void onFinish() {

                    holder.tv_time.setText("00:00:00");

                }
            }.start();




            holder.relative_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cmpny_name=homefeed.get(position).getBusiness_name();
                    ad_id=homefeed.get(position).getid();
                    Intent i = new Intent(context,Activity_ListDetail.class);
                    context.startActivity(i);

                }
            });

            return convertView;

        }

        class Holdleroffer
        {
            TextView tv_name;
            TextView tv_headingname;
            TextView tv_time;
            TextView tv_colr;
            TextView tv_remaining;
            RelativeLayout relative_list;
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
    }

}