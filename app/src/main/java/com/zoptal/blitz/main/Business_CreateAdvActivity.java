package com.zoptal.blitz.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.zoptal.blitz.model.Store_List;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Business_CreateAdvActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow,img_calander;
    private EditText ed_saledesc,ed_saleheading,ed_discount;
    private RelativeLayout rel_time,rel_endtime,rel_selectstore,rel_selectstore1,rel_date;
    private TextView select_store,tv_date,tv_endtym,tv_strttym,select_store1,select_store2;
    private Button btn_createadv;
    private RadioGroup radioGroup;
    private RadioButton radioButton_indivisualstore,radioButton_multiplestore;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
     Dialog dialog;
    ProgressDialog loading;
    public static final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    double latitude, longitude;
    GPSTracker gps;
    Context mContext;
    String access_tokn,repost;
    private List<Store_List> storeList = new ArrayList<Store_List>();
    CustomListAdapterStorelist adapter;
    RadioButton selected=null;
    boolean chk=false;
    ListView listview;
    String curnttym,selectedstore,selectedstoreid;
    boolean singleselect=true;
    List  arrayList_storeitems;
    List  arrayList_storeitems_id;
    StringBuilder commaSepValueBuilder_id;
    String ad_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_adv);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        repost = sharedpreferences1.getString("repost", "");


        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Business_CreateAdvActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, Business_CreateAdvActivity.this);

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
        img_calander=(ImageView)findViewById(R.id.img_calander);
        ed_saledesc=(EditText)findViewById(R.id.ed_saledesc);
        ed_saleheading=(EditText)findViewById(R.id.ed_saleheading);
        select_store1=(TextView) findViewById(R.id.select_store1);
        select_store2=(TextView) findViewById(R.id.select_store2);
        ed_discount=(EditText)findViewById(R.id.ed_discount);
        rel_time=(RelativeLayout)findViewById(R.id.rel_time);
        rel_endtime=(RelativeLayout)findViewById(R.id.rel_endtime);
        rel_date=(RelativeLayout)findViewById(R.id.rel_date);
        rel_selectstore=(RelativeLayout)findViewById(R.id.rel_selectstore);
        rel_selectstore1=(RelativeLayout)findViewById(R.id.rel_selectstore1);
        select_store=(TextView)findViewById(R.id.select_store);
        btn_createadv=(Button)findViewById(R.id.btn_createadv);
        tv_date=(TextView)findViewById(R.id.tv_date);
        tv_endtym=(TextView)findViewById(R.id.tv_endtym);
        tv_strttym=(TextView)findViewById(R.id.tv_strttym);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioButton_indivisualstore=(RadioButton)findViewById(R.id.radioButton_indivisualstore);
        radioButton_multiplestore=(RadioButton)findViewById(R.id.radioButton_multiplestore);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        setDateTimeField();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
       // new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm a");
        curnttym= df3.format(c.getTime());
         Log.e("current date tym--",""+curnttym);

          img_backarrow.setOnClickListener(this);
          btn_createadv.setOnClickListener(this);
          img_calander.setOnClickListener(this);
          rel_time.setOnClickListener(this);
          rel_endtime.setOnClickListener(this);
          rel_selectstore.setOnClickListener(this);
          rel_selectstore1.setOnClickListener(this);
          select_store1.setOnClickListener(this);
          select_store2.setOnClickListener(this);
          rel_date.setOnClickListener(this);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {

                    case R.id.radioButton_indivisualstore:
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        rel_selectstore.setVisibility(View.VISIBLE);
                        rel_selectstore1.setVisibility(View.GONE);
                        rel_selectstore.setGravity(Gravity.LEFT);
////                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)rel_selectstore.getLayoutParams();
////                        layoutParams.setMargins(121, 140, 0, 0);
//                        rel_selectstore.setLayoutParams(layoutParams);

                        break;

                    case R.id.radioButton_multiplestore:

                        rel_selectstore.setVisibility(View.VISIBLE);
                        rel_selectstore.setGravity(Gravity.RIGHT);
                        int selectedId2 = radioGroup.getCheckedRadioButtonId();
                        rel_selectstore1.setVisibility(View.VISIBLE);
                        rel_selectstore.setVisibility(View.GONE);


//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rel_selectstore.getLayoutParams();
//                        params.addRule(LinearLayout.ALIGN_PARENT_RIGHT);
//                        rel_selectstore.setLayoutParams(params);
//                        radiobtn2 = (RadioButton) view.findViewById(selectedId2);
//                        radiovalue= String.valueOf(radiobtn2.getText());
//
//                        type_value= String.valueOf(radiobtn2.getText());

                        break;

                }

            }
        });

        if(repost.equals("true")){

            Intent intent = getIntent();
            ad_id=intent.getStringExtra("ad_id");

            get_prev_advdetail();
        }
       else{

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

//                Intent i = new Intent(Business_CreateAdvActivity.this, Business_WillowClothingActivity.class);
//                startActivity(i);
                finish();
                break;

            case R.id.btn_createadv:


                create_adv();

                break;

            case R.id.rel_date:

                Log.e("clicked---","clndr");

                fromDatePickerDialog.show();
                break;

            case R.id.img_calander:

                Log.e("clicked---","clndrimage");
                fromDatePickerDialog.show();

                break;

            case R.id.rel_time:

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Business_CreateAdvActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String aMpM = "AM";
                        if(selectedHour >11)
                        {
                            aMpM = "PM";
                        }

                        int currentHour;
                        if(selectedHour>11)
                        {
                            currentHour = selectedHour - 12;
                        }
                        else
                        {
                            currentHour = selectedHour;
                        }


                        tv_strttym.setText(String.format("%02d:%02d %s", currentHour, selectedMinute,
                                aMpM + "\n"));
                    }
                }, hour, minute,false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();





                break;

            case R.id.rel_endtime:

                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;

                mTimePicker1 = new TimePickerDialog(Business_CreateAdvActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String aMpM = "AM";
                        if(selectedHour >11)
                        {
                            aMpM = "PM";
                        }

                        int currentHour;
                        if(selectedHour>11)
                        {
                            currentHour = selectedHour - 12;
                        }
                        else
                        {
                            currentHour = selectedHour;
                        }


                        tv_endtym.setText(String.format("%02d:%02d %s", currentHour, selectedMinute,
                                aMpM + "\n"));
                    }
                }, hour1, minute1,false);//Yes 24 hour time


                mTimePicker1.setTitle("Select Time");
                mTimePicker1.show();



                break;

            case R.id.rel_selectstore:

                singleselect=true;

                dialog_selectindviusal_store();
                break;

            case R.id.rel_selectstore1:

                singleselect=false;

                dialog_selectmultiple_store();

                break;
            case R.id.select_store1:

                singleselect=true;

                dialog_selectindviusal_store();
                break;

            case R.id.select_store2:

                singleselect=false;

                dialog_selectmultiple_store();

                break;
        }
    }


    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(Business_CreateAdvActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
               tv_date.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    public void dialog_selectindviusal_store() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        arrayList_storeitems  = new ArrayList<String>();
        arrayList_storeitems_id  = new ArrayList<String>();
        dialog = new Dialog(Business_CreateAdvActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_individualstore);

         listview=(ListView)dialog.findViewById(R.id.listview);
          listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
          Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
          Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);

        storeList.clear();
        adapter = new CustomListAdapterStorelist(storeList);
        listview.setAdapter(adapter);



        getstorelist();

        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<arrayList_storeitems.size();i++) {

                    select_store.setText(arrayList_storeitems.get(i).toString());

                }
                commaSepValueBuilder_id = new StringBuilder();

                for(int i=0;i<arrayList_storeitems_id.size();i++) {


                    commaSepValueBuilder_id.append(arrayList_storeitems_id.get(i));

                    if ( i != arrayList_storeitems_id.size()-1){
                        commaSepValueBuilder_id.append(", ");
                    }
                }
                Log.e("commaSepValueBuilder_id",""+commaSepValueBuilder_id.toString());
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void dialog_selectmultiple_store() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        arrayList_storeitems  = new ArrayList<String>();
        arrayList_storeitems_id  = new ArrayList<String>();
        dialog = new Dialog(Business_CreateAdvActivity.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_individualstore);

        listview=(ListView)dialog.findViewById(R.id.listview);
        Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
        TextView tv_head=(TextView)dialog.findViewById(R.id.tv_head);
        tv_head.setText("Multiple Store");

        storeList.clear();
        adapter = new CustomListAdapterStorelist(storeList);
        listview.setAdapter(adapter);

        getstorelist();

        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder commaSepValueBuilder = new StringBuilder();

                for(int i=0;i<arrayList_storeitems.size();i++) {
                    commaSepValueBuilder.append(arrayList_storeitems.get(i));

                    if ( i != arrayList_storeitems.size()-1){
                        commaSepValueBuilder.append(", ");
                    }
                }
                Log.e("commaSepValueBuilder",""+commaSepValueBuilder.toString());

                select_store.setText(commaSepValueBuilder.toString());
                commaSepValueBuilder_id = new StringBuilder();

                for(int i=0;i<arrayList_storeitems_id.size();i++) {
                    commaSepValueBuilder_id.append(arrayList_storeitems_id.get(i));

                    if ( i != arrayList_storeitems_id.size()-1){
                        commaSepValueBuilder_id.append(", ");
                    }
                }
                Log.e("commaSepValueBuilder_id",""+commaSepValueBuilder_id.toString());

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    class CustomListAdapterStorelist extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        private List<Store_List> storeItems;

        public CustomListAdapterStorelist(List<Store_List> storeItems) {

            this.context = context;
            this.storeItems = storeItems;
            inflater = LayoutInflater.from(getApplicationContext());

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final Holdlerstore holder = new Holdlerstore();
            convertView = inflater.inflate(R.layout.customview_storelist, null);

            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_check= (ImageView)convertView.findViewById(R.id.img_check);
            holder.rel_list=(RelativeLayout)convertView.findViewById(R.id.rel_list);
            holder.tv.setText(storeItems.get(position).getStore_name());



            if(singleselect==true) {

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    for(int i=0;i<storeItems.size();i++) {

                        if(i==position){
                            storeItems.get(i).setStatus(true);

                           }
                       else {

                            storeItems.get(i).setStatus(false);
                             }
                    }

                    selectedstore=storeItems.get(position).getStore_name();
                    arrayList_storeitems.add(selectedstore);


                    selectedstoreid=storeItems.get(position).getid();
                    arrayList_storeitems_id.clear();
                    arrayList_storeitems_id.add(selectedstoreid);

                    Log.e("selectd--"," "+selectedstore);
                    Log.e("selectd id--","  "+selectedstoreid);

                    notifyDataSetChanged();
                }
            });

            notifyDataSetChanged();


                if (storeItems.get(position).getStatus() == true) {

                    holder.img_check.setVisibility(View.VISIBLE);
                } else {
                    holder.img_check.setVisibility(View.GONE);
                }
            }


            if(singleselect==false) {

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        storeItems.get(position).setStatus(true);

                        selectedstore=storeItems.get(position).getStore_name();
                        arrayList_storeitems.add(selectedstore);
                        selectedstoreid=storeItems.get(position).getid();
                        arrayList_storeitems_id.add(selectedstoreid);
                        Log.e("selectd--"," "+selectedstore);
                        Log.e("selectd id--","  "+selectedstoreid);

                        notifyDataSetChanged();
                    }
                });

                notifyDataSetChanged();


                if (storeItems.get(position).getStatus() == true) {

                    holder.img_check.setVisibility(View.VISIBLE);
                }

            }

            return convertView;


        }

    }

    class Holdlerstore
    {

        TextView tv;
        ImageView img_check;
        RelativeLayout rel_list;
    }



    private void getstorelist() {

        loading = new ProgressDialog(Business_CreateAdvActivity.this,R.style.AppCompatAlertDialogStyle);
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
                                 storelistdata.setStatus(false);

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
                        Toast.makeText(Business_CreateAdvActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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


    private void create_adv(){

        final String saledec = ed_saledesc.getText().toString().trim();
        final String saleheading = ed_saleheading.getText().toString().trim();
        final String dt= tv_date.getText().toString().trim();
        final String strttym= tv_strttym.getText().toString().trim();
        final String endtym= tv_endtym.getText().toString().trim();
        final String selctstore= select_store.getText().toString().trim();
        final String discount= ed_discount.getText().toString().trim();

        if(saledec.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please enter your sale description.");

            alertmsg.dialog.show();

            return;
        }
        if(saleheading.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please enter your sale heading.");

            alertmsg.dialog.show();

            return;
        }


        if(strttym.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please select your start hours.");

            alertmsg.dialog.show();

            return;
        }

        if(endtym.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please select your end hours.");

            alertmsg.dialog.show();

            return;
        }
        if(dt.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please provide start date.");

            alertmsg.dialog.show();

            return;
        }


        if(discount.isEmpty())
        {
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_CreateAdvActivity.this, "Please enter your discount code.");

            alertmsg.dialog.show();

            return;
        }
        Log.e("commaSepValueBuilder_id",""+commaSepValueBuilder_id);
            if(commaSepValueBuilder_id==null){

                AlertDialogMsg alertmsg = new
                        AlertDialogMsg(Business_CreateAdvActivity.this, "Please provide store address.");

                alertmsg.dialog.show();

                return;
            }

        Intent intentpost= new Intent(Business_CreateAdvActivity.this, Business_AdvPostActivity.class);
        intentpost.putExtra("saledec",saledec);
        intentpost.putExtra("saleheading",saleheading);
        intentpost.putExtra("dt",dt);
        intentpost.putExtra("strttym",strttym);
        intentpost.putExtra("endtym",endtym);
        intentpost.putExtra("discount",discount);
        intentpost.putExtra("selctstore",selctstore);
        intentpost.putExtra("ad_id",ad_id);
        intentpost.putExtra("store_id",commaSepValueBuilder_id.toString());
        startActivity(intentpost);


//        params.put("sales_description",saledec);
//        params.put("sales_heading",saleheading);
//        params.put("start_date", dt);
//        params.put("start_hr_min",strttym);
//        params.put("end_hr_min", endtym);
//        params.put("discount_code", discount);
//        params.put("access_token",access_tokn);
//        params.put("device_time",curnttym);
//        params.put("store_id",commaSepValueBuilder_id.toString());







//
//        loading = new ProgressDialog(Business_CreateAdvActivity.this,R.style.AppCompatAlertDialogStyle);
//        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        loading.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_advertisement,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        loading.dismiss();
//
//                        //  Toast.makeText(Business_ChangePwActivity.this,"User Registration Successfully",Toast.LENGTH_LONG).show();
//
//                        Log.e("respose===",""+response);
//
//                        try {
//                            JSONObject main_obj = new JSONObject(response);
//                            String code =main_obj.getString("code");
//                            if(code.equals("201")){
//                                String message =main_obj.getString("message");
//
//                                AlertDialogMsg alertmsg = new
//                                        AlertDialogMsg(Business_CreateAdvActivity.this, message);
//
//                                alertmsg.dialog.show();
//
//                                return;
//                            }
//                            else {
//                                String message =main_obj.getString("message");
//
//                                AlertDialogMsg alertmsg = new
//                                        AlertDialogMsg(Business_CreateAdvActivity.this, message);
//
//                                alertmsg.dialog.show();
//
//                                 ed_saledesc.setText("");
//                               ed_saleheading.setText("");
//                              tv_strttym.setText("");
//                                tv_endtym.setText("");
//                               select_store.setText("");
//                                tv_date.setText("");
//                            ed_discount.setText("");
//
//
//                                //  Toast.makeText(Business_CreateAdvActivity.this,message,Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Business_CreateAdvActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("sales_description",saledec);
//                params.put("sales_heading",saleheading);
//                params.put("start_date", dt);
//                params.put("start_hr_min",strttym);
//                params.put("end_hr_min", endtym);
//                params.put("discount_code", discount);
//                params.put("access_token",access_tokn);
//                params.put("device_time",curnttym);
//                params.put("store_id",commaSepValueBuilder_id.toString());
//
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }



    private void get_prev_advdetail() {

        loading = new ProgressDialog(Business_CreateAdvActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.prev_feed_detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                            String saledesc  =obj.getString("sales_description");
                            String saleheading =obj.getString("sales_heading");
                            String  strtdate =obj.getString("start_date");
                            String  strttime =obj.getString("start_time");
                            String  endtime =obj.getString("end_time");
                            String  storename =obj.getString("store_name");
                            String discountcode=obj.getString("discount_code");
                            String storeid=obj.getString("store_id");

                            ed_saledesc.setText(saledesc);
                            //     ed_retails.set
                            ed_saleheading.setText(saleheading);
                            tv_date.setText(strtdate);
                            tv_strttym.setText(strttime);
                            tv_endtym.setText(endtime);
                            select_store.setText(storename);
                            ed_discount.setText(discountcode);

                            radioButton_indivisualstore.setChecked(true);
                            commaSepValueBuilder_id = new StringBuilder();

                            commaSepValueBuilder_id.append(storeid);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_CreateAdvActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",access_tokn);
                map.put("ad_id",ad_id);
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }






}
