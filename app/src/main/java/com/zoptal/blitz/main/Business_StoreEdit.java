package com.zoptal.blitz.main;

import android.Manifest;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.GPSTracker;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Business_StoreEdit extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow;
    private EditText ed_storename,ed_address,ed_phoneno,ed_stime,ed_endtime;
    private RelativeLayout btn_confirm;
    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    String store_id,store_name,store_address,store_phoneno,store_stime,store_endtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }


    public void initview(){

        Intent intent = getIntent();
        store_id=intent.getStringExtra("Store_id");
        store_name=intent.getStringExtra("store_name");
        store_address=intent.getStringExtra("store_address");
        store_phoneno=intent.getStringExtra("store_phoneno");
        store_stime=intent.getStringExtra("store_stime");
        store_endtime=intent.getStringExtra("store_endtime");



        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
        btn_confirm=(RelativeLayout)findViewById(R.id.btn_confirm);
        ed_storename=(EditText)findViewById(R.id.ed_storename);
        ed_address=(EditText)findViewById(R.id.ed_address);
        ed_phoneno=(EditText)findViewById(R.id.ed_phoneno);
        ed_stime=(EditText)findViewById(R.id.ed_stime);
        ed_endtime=(EditText)findViewById(R.id.ed_endtime);
        ed_stime.setInputType(InputType.TYPE_NULL);
        ed_stime.setFocusable(false);
        ed_stime.setInputType(InputType.TYPE_NULL);
        ed_stime.setFocusable(false);


        img_backarrow.setOnClickListener(this);
        ed_stime.setOnClickListener(this);
        ed_endtime.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

        ed_storename.setText(store_name);
        ed_address.setText(store_address);
        ed_phoneno.setText(store_phoneno);
        ed_stime.setText(store_stime);
        ed_endtime.setText(store_endtime);

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Business_StoreEdit.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Business_StoreEdit.this);

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

                    gps = new GPSTracker(mContext, Business_StoreEdit.this);

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

                Intent i = new Intent(Business_StoreEdit.this, Business_businessstoreActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.ed_stime:

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

                mTimePicker = new TimePickerDialogs(Business_StoreEdit.this,new TimePickerDialog.OnTimeSetListener() {
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
                            minutes = "0" + selectedMinute+15;
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


//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ed_stime.getWindowToken(), 0);
//
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//
//                mTimePicker = new TimePickerDialog(Business_StoreEdit.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
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
//
//                        ed_stime.setText(String.format("%02d:%02d %s", currentHour, selectedMinute,
//                                aMpM + "\n"));
//                    }
//                }, hour, minute,false);//Yes 24 hour time
//
//
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();

                break;


            case R.id.ed_endtime:

                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(ed_endtime.getWindowToken(), 0);

                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);


                TimePickerDialogs mTimePicker1;

                mTimePicker = new TimePickerDialogs(Business_StoreEdit.this,new TimePickerDialog.OnTimeSetListener() {
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
                }, hour1, minute1,false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


//
//                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm1.hideSoftInputFromWindow(ed_stime.getWindowToken(), 0);
//
//                Calendar mcurrentTime1 = Calendar.getInstance();
//                int hour1 = mcurrentTime1.get(Calendar.HOUR);
//                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker1;
//
//                mTimePicker1 = new TimePickerDialog(Business_StoreEdit.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
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
//
//                        ed_endtime.setText(String.format("%02d:%02d %s", currentHour, selectedMinute,
//                                aMpM + "\n"));
//                    }
//                }, hour1, minute1,false);//Yes 24 hour time
//
//
//                mTimePicker1.setTitle("Select Time");
//                mTimePicker1.show();
                break;

            case R.id.btn_confirm:


                if (NetworkConnection.isConnectedToInternet(Business_StoreEdit.this)) {
                    edit_store();

                }
                else {
                    Toast.makeText(Business_StoreEdit.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

                break;
        }
    }

    private void edit_store(){

        final String name = ed_storename.getText().toString().trim();
        final String address = ed_address.getText().toString().trim();
        final String phone= ed_phoneno.getText().toString().trim();
        final String stime = ed_stime.getText().toString().trim();
        final String etime = ed_endtime.getText().toString().trim();


        loading = new ProgressDialog(Business_StoreEdit.this,R.style.AppCompatAlertDialogStyle);
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

                            Toast.makeText(Business_StoreEdit.this,message,Toast.LENGTH_LONG).show();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_StoreEdit.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("store_id",store_id);
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
}
