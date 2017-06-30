package com.zoptal.blitz.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.common.GooglePlacesAutocompleteAdapter;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.model.Store_List;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business_businessstoreActivity extends AppCompatActivity implements

        View.OnClickListener,AdapterView.OnItemClickListener {

    private EditText ed_name,ed_phone,ed_stime,ed_endtime;
    AutoCompleteTextView ed_address;
    private Button btn_save;
    private ImageView img_backarrow;
    private TextView tv_add,tv_msgnostore;
    public Dialog dialog;
    private ListView listview;
    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    String Store_id,store_name,store_address,store_phoneno,store_stime,store_endtime;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    CustomListAdapterStore adapter;
    private List<Store_List> storeList = new ArrayList<Store_List>();
     int dltposition;
    private Calendar mCalen;
    private int hourOfDay;
    private int minute;
    private int ampm;
    //private static final int Time_PICKER_ID = 0;
//    TimePicker timePicker;
//    private int TIME_PICKER_INTERVAL =30;
//    NumberPicker minutePicker;
//    List<String> displayedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_store);
//
//        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");


        mCalen = Calendar.getInstance();
        hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
        minute = mCalen.get(Calendar.MINUTE);
        ampm = mCalen.get(Calendar.AM_PM);

        initview();
    }

    public void initview() {

        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
        tv_add = (TextView) findViewById(R.id.tv_add);
        listview=(ListView)findViewById(R.id.listview);
        tv_msgnostore=(TextView)findViewById(R.id.tv_msgnostore);

        adapter = new CustomListAdapterStore(storeList);
        listview.setAdapter(adapter);

        getstorelist();
        img_backarrow.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Business_businessstoreActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Business_businessstoreActivity.this);

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

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, Business_businessstoreActivity.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                      latitude = gps.getLatitude();
                       longitude = gps.getLongitude();

                        // \n is for new line
                      //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_businessstoreActivity.this, Business_AccountsettingActivity.class);
                startActivity(i);
                finish();
                break;


            case R.id.tv_add:

                dialogaddstore();

                break;
        }
    }


    public void dialogaddstore() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_businessstoreActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addstore);


        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        ed_name = (EditText) dialog.findViewById(R.id.ed_name);
        ed_address = (AutoCompleteTextView) dialog.findViewById(R.id.ed_address);
        ed_phone = (EditText) dialog.findViewById(R.id.ed_phone);
         ed_stime = (EditText) dialog.findViewById(R.id.ed_stime);
       ed_endtime = (EditText) dialog.findViewById(R.id.ed_endtime);
       // RelativeLayout relative_endtime=(RelativeLayout)dialog.findViewById(R.id.relative_endtime);
       btn_save = (Button) dialog.findViewById(R.id.btn_save);

        ed_address.setAdapter(new GooglePlacesAutocompleteAdapter(Business_businessstoreActivity.this, R.layout.list_item1));

        ed_stime.setInputType(InputType.TYPE_NULL);
        ed_stime.setFocusable(false);
        ed_endtime.setInputType(InputType.TYPE_NULL);
        ed_endtime.setFocusable(false);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR);
        int minute = mcurrentTime.get(Calendar.MINUTE);





//        ed_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showDialog(Time_PICKER_ID);
//            }
//        });

//        timePicker = (TimePicker)dialog.findViewById(R.id.timePicker1);
//
//        timePicker.setIs24HourView(true);
//        timePicker.setCurrentHour(0);
//        timePicker.setCurrentMinute(0);
//
//        setTimePickerInterval(timePicker);
        ed_stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed_stime.getWindowToken(), 0);

//                MyTimePicker tp = new MyTimePicker(Business_businessstoreActivity.this);
//                tp.setIs24HourView(true);
//                Calendar cal = Calendar.getInstance(); // Current time
//                tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
//                tp.setCurrentMinute(cal.get(Calendar.MINUTE)/5);

                 Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialogs mTimePicker;

                mTimePicker = new TimePickerDialogs(Business_businessstoreActivity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {




//
//                        String aMpM = "AM";
//                        if(selectedHour >11)
//                        {
//                            aMpM = "PM";
//                        }
//
//                        int currentHour;
//                        if(selectedHour>11)
//                        {
//                            currentHour = selectedHour - 12;
//                        }
//                        else
//                        {
//                            currentHour = selectedHour;
//                        }

                        String timeSet = "";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            timeSet = "PM";
                        } else if (selectedHour == 0) {
                            selectedHour += 12;
                            timeSet = "AM";
                        } else if (selectedHour == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";


                        String minutes = "";
                        if (selectedMinute < 10)
                            minutes = "0" + selectedMinute;
                        else
                            minutes = String.valueOf(selectedMinute);

                        String aTime = new StringBuilder().append(selectedHour).append(':')
                                .append(minutes).append(" ").append(timeSet).toString();
                        ed_stime.setText(aTime);

//                        ed_endtime.setText(String.format("%02d:%02d %s", selectedHour, minutes,
//                                timeSet + "\n"));
                    }
                }, hour, minute,false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


//            new IntervalTimePickerDialog(Business_businessstoreActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
//                    // TODO Auto-generated method stub
//                    StringBuilder output = new StringBuilder().append(selectedHour).append(":").append(selectedMinute);
//                    System.out.println(output.toString());
//                }}, hour, minute, true).show();


                //TimePickerDialog mTimePicker;

          //     new IntervalTimePickerDialog(Business_businessstoreActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,TimePickerListener,hour,minute,true);
//
//                mTimePicker = new TimePickerDialog(Business_businessstoreActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                        String aMpM = "AM";
//                        if(selectedHour >11)
//                        {
//                            aMpM = "PM";
//                        }
//
//                        int currentHour;
//                        if(selectedHour>11)
//                        {
//                            currentHour = selectedHour - 12;
//                        }
//                        else
//                        {
//                            currentHour = selectedHour;
//                        }
//
//                        ed_stime.setText(String.format("%02d:%02d %s", currentHour, selectedMinute,
//                                aMpM + "\n"));
//                    }
//                }, hour, minute,false);//Yes 24 hour time
//
//
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();

            }
        });

        ed_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed_endtime.getWindowToken(), 0);

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialogs mTimePicker;

                mTimePicker = new TimePickerDialogs(Business_businessstoreActivity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                        String aMpM = "AM";
//                        if(selectedHour >11)
//                        {
//                            aMpM = "PM";
//                        }
//
//                        int currentHour;
//                        if(selectedHour>11)
//                        {
//                            currentHour = selectedHour - 12;
//                        }
//                        else
//                        {
//                            currentHour = selectedHour;
//                        }

                        String timeSet = "";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            timeSet = "PM";
                        } else if (selectedHour == 0) {
                            selectedHour += 12;
                            timeSet = "AM";
                        } else if (selectedHour == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";

                        String minutes = "";
                        if (selectedMinute < 10)
                            minutes = "0" + selectedMinute;
                        else
                            minutes = String.valueOf(selectedMinute);

                        String aTime = new StringBuilder().append(selectedHour).append(':')
                                .append(minutes).append(" ").append(timeSet).toString();
                        ed_endtime.setText(aTime);

//                        ed_endtime.setText(String.format("%02d:%02d %s", selectedHour, minutes,
//                                timeSet + "\n"));
                    }
                }, hour, minute,false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentstore = new Intent(Business_businessstoreActivity.this, Business_businessstoreActivity.class);
                startActivity(intentstore);
                dialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.isConnectedToInternet(Business_businessstoreActivity.this)) {
                    add_store();

                }
                else {
                    Toast.makeText(Business_businessstoreActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });

        dialog.show();

    }
//    private void setTimePickerInterval(TimePicker timePicker) {
//        try {
//            Class<?> classForid = Class.forName("com.android.internal.R$id");
//            // Field timePickerField = classForid.getField("timePicker");
//
//            Field field = classForid.getField("minute");
//            minutePicker = (NumberPicker) timePicker
//                    .findViewById(field.getInt(null));
//
//            minutePicker.setMinValue(0);
//            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
//            displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            //  for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//            //      displayedValues.add(String.format("%02d", i));
//            //  }
//            minutePicker.setDisplayedValues(displayedValues
//                    .toArray(new String[0]));
//            minutePicker.setWrapSelectorWheel(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private TimePickerDialog.OnTimeSetListener TimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {

                // while dialog box is closed, below method is called.
                public void onTimeSet(TimePicker view, int hour, int minute) {

                    mCalen.set(Calendar.HOUR_OF_DAY, hour);
                    mCalen.set(Calendar.MINUTE, minute);

                    int hour12format = mCalen.get(Calendar.HOUR);
                    hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                    minute = mCalen.get(Calendar.MINUTE);
                    ampm = mCalen.get(Calendar.AM_PM);
                    String ampmStr = (ampm == 0) ? "AM" : "PM";
                    // Set the Time String in Button
                    ed_name.setText(hour12format + " : " + minute + "  " + ampmStr);
                }
            };

    class CustomListAdapterStore extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
       // String[] historyname;

        private List<Store_List> storeItems;

        public CustomListAdapterStore(List<Store_List> storeItems) {

            this.context = context;
            this.storeItems = storeItems;
            inflater = LayoutInflater.from(getApplicationContext());

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            Log.e("storeitem size---",""+storeItems.size());
            if(storeItems.size()==0){

               tv_msgnostore.setVisibility(View.VISIBLE);
            }
            else{
                tv_msgnostore.setVisibility(View.GONE);
            }
            return storeItems.size();

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

            Holdlerstore holder = new Holdlerstore();
            convertView = inflater.inflate(R.layout.customview_store, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img_repost);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.rel_list=(RelativeLayout)convertView.findViewById(R.id.rel_list);
            holder.rel_dltstore=(RelativeLayout)convertView.findViewById(R.id.rel_dltstore);

            Store_id=storeItems.get(position).getid();
            store_name=storeItems.get(position).getStore_name();
            store_address=storeItems.get(position).getStore_address();
            store_phoneno=storeItems.get(position).getStore_phone();
            store_stime=storeItems.get(position).getStore_hours_from();
            store_endtime=storeItems.get(position).getStore_hours_to();
            holder.tv.setText(storeItems.get(position).getStore_name());
            holder.tv_address.setText(storeItems.get(position).getStore_address());


            holder.rel_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intentstore = new Intent(Business_businessstoreActivity.this, Business_StoreEdit.class);
                    intentstore.putExtra("Store_id",Store_id);
                    intentstore.putExtra("store_name",store_name);
                    intentstore.putExtra("store_address",store_address);
                    intentstore.putExtra("store_phoneno",store_phoneno);
                    intentstore.putExtra("store_stime",store_stime);
                    intentstore.putExtra("store_endtime",store_endtime);
                    startActivity(intentstore);

                }
            });

            holder.rel_dltstore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Store_id=storeItems.get(position).getid();
                    dialogdltconfirmation(position);
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;

        }

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
        dialog = new Dialog(Business_businessstoreActivity.this, android.R.style.Theme_Translucent);
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

                deletestore(pos);
                adapter.notifyDataSetChanged();

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


    private void add_store(){

        final String name = ed_name.getText().toString().trim();
        final String address = ed_address.getText().toString().trim();
        final String phone= ed_phone.getText().toString().trim();
        final String stime = ed_stime.getText().toString().trim();
        final String etime = ed_endtime.getText().toString().trim();


        if(name.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businessstoreActivity.this, "Please Provide store name.");

            alertmsg.dialog.show();
            return;
        }

        if(address.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businessstoreActivity.this, "Please Provide store address.");

            alertmsg.dialog.show();
            return;
        }
        if(phone.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businessstoreActivity.this, "Please Provide store Phone.");

            alertmsg.dialog.show();
            return;
        }

        if(stime.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businessstoreActivity.this, "Please Provide store hours from.");

            alertmsg.dialog.show();
            return;
        }

        if(etime.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businessstoreActivity.this, "Please Provide store hours to.");

            alertmsg.dialog.show();
            return;
        }

        loading = new ProgressDialog(Business_businessstoreActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_edit_store,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                              Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String code =main_obj.getString("code");
                            String message =main_obj.getString("message");

                            Toast.makeText(Business_businessstoreActivity.this,message,Toast.LENGTH_LONG).show();

                            ed_name.setText("");
                            ed_address.setText("");
                            ed_phone.setText("");
                            ed_stime.setText("");
                            ed_endtime.setText("");

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_businessstoreActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("store_id","");
                params.put("store_name",name);
                params.put("store_address",address);
                params.put("store_phone", phone);
                params.put("store_hours_from", stime);
                params.put("store_hours_to", etime);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude",String.valueOf(longitude));
                params.put("access_token",access_tokn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    class Holdlerstore
    {
        ImageView img;
        TextView tv;
        TextView tv_address;
        RelativeLayout rel_list;
        RelativeLayout rel_dltstore;
    }



    private void getstorelist() {

        loading = new ProgressDialog(Business_businessstoreActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.store_list,
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

                                Store_List storelistdata = new Store_List();

                                storelistdata.setStore_name(obj.getString("store_name"));
                                storelistdata.setId(obj.getString("id"));
                                storelistdata.setStore_address(obj.getString("store_address"));
                                storelistdata.setStore_phone(obj.getString("store_phone"));
                                storelistdata.setStore_hours_from(obj.getString("store_hours_from"));
                                storelistdata.setStore_hours_to(obj.getString("store_hours_to"));
                                storelistdata.setLatitude(obj.getString("latitude"));
                                storelistdata.setLongitude(obj.getString("longitude"));


// adding storelistdata to storeList array
                                storeList.add(storelistdata);

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
                        Toast.makeText(Business_businessstoreActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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



    private void deletestore(final int pos) {

        loading = new ProgressDialog(Business_businessstoreActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.delete_store,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);
                        Log.e("posss",""+pos);
                        storeList.remove(pos);
                        adapter.notifyDataSetChanged();
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
                        Toast.makeText(Business_businessstoreActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);
                map.put("store_id",Store_id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}




