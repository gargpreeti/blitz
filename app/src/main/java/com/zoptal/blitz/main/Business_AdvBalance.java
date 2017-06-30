package com.zoptal.blitz.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.model.Card_type;
import com.zoptal.blitz.model.Package_List;
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

public class Business_AdvBalance extends AppCompatActivity implements View.OnClickListener {

      private ImageView img_backarrow;
      private TextView tv_remaining_credits,tv_packageoption;
      private  Custom_pageradapter mCustomPagerAdapter;
      private  ViewPager mViewPager;
      private  Button btn_purchase;
      private List<Package_List> packageList = new ArrayList<Package_List>();

      private Dialog dialog,dialognewcard,dialogoldcard;
      ProgressDialog loading;
      int currentPage;
      private EditText ed_firstname,ed_exp,ed_lastname,ed_cardnum,ed_expy,ed_cardtype,ed_cvv;
      private ImageView img_newcard,img_newcard1;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn,savecrd="0";
    TextView tv_price;
    Spinner list_mnth;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
   // public static String [] cardtypedata={"Visa","MasterCard","American Express"};

    ArrayList<Card_type> arrayofcardtype = new ArrayList<Card_type>();
    CustomListAdapterCardtype adapter;
    String text;
    String cardid,cardnumber;
     int cardstatus;
    Button b;
    static Dialog d ;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    ListView listview;
    Boolean statuscrdchk=false;
String oldcardnumber="";
    String selectedprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_balance);

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");
        initview();
    }


    public void initview(){

        Intent intent = getIntent();
       String  remainingpoints=intent.getStringExtra("points");


        arrayofcardtype.add(new Card_type("Visa",false));
        arrayofcardtype.add(new Card_type("MasterCard",false));
        arrayofcardtype.add(new Card_type("American Express",false));

       img_backarrow=(ImageView)findViewById(R.id.img_backarrow);
       mCustomPagerAdapter = new Custom_pageradapter(Business_AdvBalance.this,packageList);
       mViewPager = (ViewPager)findViewById(R.id.pager);
       mViewPager.setAdapter(mCustomPagerAdapter);

        tv_remaining_credits=(TextView)findViewById(R.id.tv_remaining_credits);
        tv_packageoption=(TextView)findViewById(R.id.tv_packageoption);
        btn_purchase=(Button)findViewById(R.id.btn_purchase);


        tv_remaining_credits.setText(remainingpoints);

        img_backarrow.setOnClickListener(this);
        btn_purchase.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("yyyy", Locale.US);
       // setDateTimeField();

        getpackagelist();
        savecardlist();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_AdvBalance.this, Business_WillowClothingActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_purchase:

                Log.e("selectedprice===",""+selectedprice);


                if(selectedprice.equals("Free")){

                    freepayment();
                }
                else {
                    if (cardstatus == 0) {
                        dialog_add_new_card();
                    } else {
                        dialogcard();
                    }
                }
                break;


        }
    }
  class Custom_pageradapter extends PagerAdapter {

    private List<Package_List> packageItems;
    Context mContext;
    LayoutInflater mLayoutInflater;

    int[] mResources = {
            R.drawable.gold,
            R.drawable.adv_back,
            R.drawable.adv_back,
            R.drawable.adv_back,
            R.drawable.adv_back,

    };

    public Custom_pageradapter(Context context,List<Package_List> packageItems) {
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
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        RelativeLayout imageView = (RelativeLayout) itemView.findViewById(R.id.imageView);
        TextView tv_option=(TextView)itemView.findViewById(R.id.tv_option);
        TextView tv_adv_name=(TextView)itemView.findViewById(R.id.tv_adv_name);
        TextView tv_credit=(TextView)itemView.findViewById(R.id.tv_credit);
        TextView tv_dlr=(TextView)itemView.findViewById(R.id.tv_dlr);
        TextView tv_cad=(TextView)itemView.findViewById(R.id.tv_cad);
        tv_price=(TextView)itemView.findViewById(R.id.tv_price);

        tv_option.setText(packageItems.get(position).getTitle());
        tv_adv_name.setText(packageItems.get(position).getSubtitle());
        tv_credit.setText(packageItems.get(position).getPoints());
        tv_price.setText(packageItems.get(position).getPrice());

        Log.e("selected posttt",""+position);




        if(tv_price.getText().toString().equals("Free")){

            tv_dlr.setVisibility(View.GONE);
            tv_cad.setVisibility(View.GONE);
        }
        else {
            tv_dlr.setVisibility(View.VISIBLE);
            tv_cad.setVisibility(View.VISIBLE);
        }

        imageView.setBackgroundResource(mResources[position]);

        Log.e("position----",""+position);




        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                currentPage = mViewPager.getCurrentItem();
              Log.e("current page=====",""+currentPage);
                if(currentPage==0){

                    selectedprice=packageItems.get(0).getPrice();
                    Log.e("seledprice---",""+selectedprice);
                    tv_packageoption.setText("1");
                }
                else if(currentPage==1){

                    selectedprice=packageItems.get(1).getPrice();
                    Log.e("seledprice---",""+selectedprice);
                    tv_packageoption.setText("2");
                }
                else  if(currentPage==2){
                    selectedprice=packageItems.get(2).getPrice();
                    Log.e("seledprice---",""+selectedprice);
                    tv_packageoption.setText("3");

                }
                else  if(currentPage==3){
                    selectedprice=packageItems.get(3).getPrice();
                    Log.e("seledprice---",""+selectedprice);
                    tv_packageoption.setText("4");
                }
                else if(currentPage==4){
                    selectedprice=packageItems.get(4).getPrice();
                    Log.e("seledprice---",""+selectedprice);
                    tv_packageoption.setText("5");
                }
            }

            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                currentPage = mViewPager.getCurrentItem();

                Log.e("current page......",""+currentPage);
            }
        });


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}


    public void dialogcard() {

        dialog = new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addcard);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        ImageView  img_newcard=(ImageView) dialog.findViewById(R.id.img_newcard);
        TextView tv_new_card=(TextView)dialog.findViewById(R.id.tv_new_card);
        TextView tv_oldcard=(TextView)dialog.findViewById(R.id.tv_oldcard);
        ImageView  img_oldcard=(ImageView) dialog.findViewById(R.id.img_oldcard);

        RelativeLayout rel_useoldcard=(RelativeLayout)dialog.findViewById(R.id.rel_useoldcard);

        EditText  ed_cardnum = (EditText) dialog.findViewById(R.id.ed_cardnum);


        ed_cardnum.setText(oldcardnumber);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);



        tv_new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog_add_new_card();
            }
        });
        img_newcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog_add_new_card();
            }
        });
        tv_oldcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog_use_old_card();
            }
        });
        img_oldcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog_use_old_card();
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

                if (NetworkConnection.isConnectedToInternet(Business_AdvBalance.this)) {

                    useoldcard();
                }
                else {
                    Toast.makeText(Business_AdvBalance.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });


//        rel_useoldcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        dialog.show();

    }

    public void dialog_add_new_card() {

        dialognewcard = new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
        dialognewcard.setCanceledOnTouchOutside(true);
        dialognewcard.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialognewcard.setCancelable(true);
        dialognewcard.setContentView(R.layout.dialog_addnewcard);

        ImageView img_cross = (ImageView) dialognewcard.findViewById(R.id.img_cross);
        ed_firstname=(EditText)dialognewcard.findViewById(R.id.ed_firstname);
       ed_lastname=(EditText)dialognewcard.findViewById(R.id.ed_lastname);
       ed_cardnum=(EditText)dialognewcard.findViewById(R.id.ed_cardnum);
       ed_exp=(EditText) dialognewcard.findViewById(R.id.ed_exp);

       ed_expy=(EditText)dialognewcard.findViewById(R.id.ed_expy);
       ed_cardtype=(EditText)dialognewcard.findViewById(R.id.ed_cardtype);
       ed_cvv=(EditText)dialognewcard.findViewById(R.id.ed_cvv);
       img_newcard=(ImageView)dialognewcard.findViewById(R.id.img_newcard);
        img_newcard1=(ImageView)dialognewcard.findViewById(R.id.img_newcard1);
        Button btn_submit = (Button) dialognewcard.findViewById(R.id.btn_submit);
        list_mnth=(Spinner)dialognewcard.findViewById(R.id.list_mnth);





        ed_cardtype.setInputType(InputType.TYPE_NULL);
        ed_cardtype.setFocusable(false);
        ed_cardtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cardtype();
            }
        });

        ed_exp.setInputType(InputType.TYPE_NULL);
        ed_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_exp.setVisibility(View.INVISIBLE);
                list_mnth.setVisibility(View.VISIBLE);
            }
        });




        String[] items = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Business_AdvBalance.this, android.R.layout.simple_spinner_dropdown_item, items);
        list_mnth.setAdapter(adapter);
        //   list_mnth.setPrompt("Exp.[MM]");
        list_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

               // ed_exp.setText(list_mnth.getSelectedItem().toString().trim());
                ((TextView) list_mnth.getSelectedView()).setTextColor(getResources().getColor(R.color.colorblack));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ed_expy.setInputType(InputType.TYPE_NULL);
      ed_expy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      showYearDialog();

        // fromDatePickerDialog.show();


     //   setDateTimeField();
//        MonthYearPickerDialog pd = new MonthYearPickerDialog();
//        pd.setListener(new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                String formatedDate = "";
////
////                if(selectedYear == 1904)
////                {
////                    String currentDateFormat = selectedMonth + "/" + selectedDay;// + "/" + selectedYear;  //"MM/dd/yyyy"
////                    formatedDate = DateTimeOp.oneFormatToAnother(currentDateFormat, Constants.dateFormat20, Constants.dateFormat24);
////                }
////                else{
////                    String currentDateFormat = selectedMonth + "/" + selectedDay + "/" + selectedYear;  //"MM/dd/yyyy"
////                    formatedDate = DateTimeOp.oneFormatToAnother(currentDateFormat, Constants.dateFormat0, Constants.dateFormat21);
////                }
////
////                etBirthday.setText(formatedDate);
//            }
//        });
//        pd.show(getFragmentManager(), "MonthYearPickerDialog");
    }
});



        img_newcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_newcard.setVisibility(View.GONE);
                img_newcard1.setVisibility(View.VISIBLE);
                savecrd="1";
            }
        });

        img_newcard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_newcard.setVisibility(View.VISIBLE);
                img_newcard1.setVisibility(View.GONE);
                savecrd="0";
            }
        });
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialognewcard.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.isConnectedToInternet(Business_AdvBalance.this)) {

                    makepayment();
                }
                else {
                    Toast.makeText(Business_AdvBalance.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });

        dialognewcard.show();

    }
    public void dialog_use_old_card() {

        dialogoldcard = new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
        dialogoldcard.setCanceledOnTouchOutside(true);
        dialogoldcard.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogoldcard.setCancelable(true);
        dialogoldcard.setContentView(R.layout.dialog_useoldcard);

        ImageView img_cross = (ImageView) dialogoldcard.findViewById(R.id.img_cross);

        final TextView tv_crdnum=(TextView)dialogoldcard.findViewById(R.id.tv_crdnum);
        Button btn_confirm = (Button) dialogoldcard.findViewById(R.id.btn_confirm);
        Button btn_cncl = (Button) dialogoldcard.findViewById(R.id.btn_cncl);

        tv_crdnum.setText(cardnumber);

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoldcard.dismiss();
            }
        });

        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoldcard.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldcardnumber=tv_crdnum.getText().toString().trim();
                if (NetworkConnection.isConnectedToInternet(Business_AdvBalance.this)) {

                    useoldcard();
                }
                else {
                    Toast.makeText(Business_AdvBalance.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });

        dialogoldcard.show();

    }


    private void getpackagelist() {

        loading = new ProgressDialog(Business_AdvBalance.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.packagelist,
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

                                Package_List packagelistdata = new Package_List();

                                packagelistdata.setId(obj.getString("id"));
                                packagelistdata.setTitle(obj.getString("title"));
                                packagelistdata.setSubtitle(obj.getString("subtitle"));
                                packagelistdata.setPoints(obj.getString("points"));
                                packagelistdata.setPrice(obj.getString("price"));


// adding storelistdata to storeList array
                                packageList.add(packagelistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mCustomPagerAdapter.notifyDataSetChanged();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvBalance.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

               // map.put("access_token",access_tokn);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void makepayment(){

        final String firstname = ed_firstname.getText().toString().trim();
        final String lastname = ed_lastname.getText().toString().trim();
        final String cardnum= ed_cardnum.getText().toString().trim();
        final String expm = list_mnth.getSelectedItem().toString().trim();
        final String expy = ed_expy.getText().toString().trim();
        final String crdtype = ed_cardtype.getText().toString().trim();
        final String cvv_value = ed_cvv.getText().toString().trim();

          final String packgeid= tv_packageoption.getText().toString();

//        Log.e("tv_price=====",""+tv_price.getText().toString());
//          final String amount=tv_price.getText().toString();


        if(firstname.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide First Name as on Credit Card.");

            alertmsg.dialog.show();
            return;
        }

        if(lastname.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide Last Name as on Credit Card.");

            alertmsg.dialog.show();
            return;
        }

        if(crdtype.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide Credit Card Type.");

            alertmsg.dialog.show();
            return;
        }

        if(cardnum.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide Credit Card Number.");

            alertmsg.dialog.show();
            return;
        }

        if(expy.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide Expire Date Year.");

            alertmsg.dialog.show();
            return;
        }

        if(cvv_value.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide CVV.");

            alertmsg.dialog.show();
            return;
        }
        if(expm.isEmpty()){
            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_AdvBalance.this, "Please Provide Expire Month.");

            alertmsg.dialog.show();
            return;
        }

        loading = new ProgressDialog(Business_AdvBalance.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.makepayment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        try {
                            JSONObject main_obj = new JSONObject(response);

                          String   message  =main_obj.getString("message");
                            String   remaining_credits  =main_obj.getString("remaining_credits");
                            tv_remaining_credits.setText(remaining_credits);
                          //  Toast.makeText(Business_AdvBalance.this,message,Toast.LENGTH_LONG).show();
                            dialognewcard.dismiss();


                         final Dialog   dialogsuccess= new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
                            dialogsuccess.setCanceledOnTouchOutside(true);
                            dialogsuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogsuccess.setCancelable(true);
                            dialogsuccess.setContentView(R.layout.alertdialogmsgsuccess);

                            ImageView img_cross = (ImageView) dialogsuccess.findViewById(R.id.img_cross);
                            ImageView img_success = (ImageView) dialogsuccess.findViewById(R.id.img_success);

                            TextView tv_msg=(TextView)dialogsuccess.findViewById(R.id.tv_msg);

                             tv_msg.setText(message);

                            img_cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsuccess.dismiss();
                                }
                            });
                            img_success.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsuccess.dismiss();
                                }
                            });


                            dialogsuccess.show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("respose===",""+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvBalance.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("access_token",access_tokn);
                params.put("package_id",packgeid);
                params.put("amount",selectedprice);
                params.put("payment_by", "new_card");
                params.put("cc_fname", firstname);
                params.put("cc_lname",lastname);
                params.put("cc_number",cardnum );
                params.put("cvv",cvv_value);
                params.put("exp_month",expm);
                params.put("exp_year",expy);
                params.put("card_type",crdtype );
                params.put("save_credit_card",savecrd);
                params.put("save_cc_id","");

                Log.e("payment param---",""+params);
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private void freepayment(){

        final String packgeid= tv_packageoption.getText().toString();

        loading = new ProgressDialog(Business_AdvBalance.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.free_package,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        try {
                            JSONObject main_obj = new JSONObject(response);

                            String   message  =main_obj.getString("message");
                            String   remaining_credits  =main_obj.getString("remaining_credits");
                            tv_remaining_credits.setText(remaining_credits);
                            //  Toast.makeText(Business_AdvBalance.this,message,Toast.LENGTH_LONG).show();
                           // dialognewcard.dismiss();


                            final Dialog   dialogsuccess= new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
                            dialogsuccess.setCanceledOnTouchOutside(true);
                            dialogsuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogsuccess.setCancelable(true);
                            dialogsuccess.setContentView(R.layout.alertdialogmsgsuccess);

                            ImageView img_cross = (ImageView) dialogsuccess.findViewById(R.id.img_cross);
                            ImageView img_success = (ImageView) dialogsuccess.findViewById(R.id.img_success);

                            TextView tv_msg=(TextView)dialogsuccess.findViewById(R.id.tv_msg);

                            tv_msg.setText(message);

                            img_cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsuccess.dismiss();
                                }
                            });
                            img_success.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsuccess.dismiss();
                                }
                            });


                            dialogsuccess.show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("respose free package===",""+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvBalance.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("access_token",access_tokn);
                params.put("package_id",packgeid);


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private void savecardlist() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.list_save_cards,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Log.e("save card respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            cardstatus= (int) main_obj.get("card_status");

                            Log.e("card_status",""+cardstatus);

                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                              cardid=obj.getString("id");
                                 cardnumber=obj.getString("card_number");

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
                        Toast.makeText(Business_AdvBalance.this,error.toString(),Toast.LENGTH_LONG ).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

//    private void setDateTimeField() {
//
//        final Calendar newCalendar = Calendar.getInstance();
//        fromDatePickerDialog = new DatePickerDialog(Business_AdvBalance.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
//
//
//
//            public void onDateSet(DatePicker view, int year) {
//                Calendar newDate = Calendar.getInstance();
//                int year1 = newCalendar.get(Calendar.YEAR);
//               // newDate.set(year1);
//                ed_expy.setText(dateFormatter.format(newDate.getTime()));
//
//            }
//
//        }, newCalendar.get(Calendar.YEAR),null,null);
//
//
//    }


    public void dialog_cardtype() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_AdvBalance.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cardtype);

        TextView tv_head=(TextView)dialog.findViewById(R.id.tv_head);
        tv_head.setText("Select");
         listview=(ListView)dialog.findViewById(R.id.listview);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);

        adapter = new CustomListAdapterCardtype(Business_AdvBalance.this,arrayofcardtype);
        listview.setAdapter(adapter);


        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_cardtype.setText(text);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showYearDialog()
    {

        final Dialog d = new Dialog(Business_AdvBalance.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
              //  b.setText(String.valueOf(nopicker.getValue()));
                ed_expy.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }
    private void useoldcard(){

        final String packgeid= tv_packageoption.getText().toString();
      //  final String amount=tv_price.getText().toString();

        loading = new ProgressDialog(Business_AdvBalance.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.makepayment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        try {
                            JSONObject main_obj = new JSONObject(response);

                            String   message  =main_obj.getString("message");
                            String   remaining_credits  =main_obj.getString("remaining_credits");
                            tv_remaining_credits.setText(remaining_credits);
                            //  Toast.makeText(Business_AdvBalance.this,message,Toast.LENGTH_LONG).show();
                            Toast.makeText(Business_AdvBalance.this,message,Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("respose oldcrd===",""+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_AdvBalance.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("access_token",access_tokn);
                params.put("package_id",packgeid);
                params.put("amount",selectedprice);
                params.put("save_cc_id",cardid);
                params.put("payment_by", "old_card");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public class CustomListAdapterCardtype extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        ArrayList<Card_type> arraylist;

        public CustomListAdapterCardtype(Context context, ArrayList<Card_type> arraylist) {

            this.context = context;
            this.arraylist=arraylist;
            inflater = LayoutInflater.from(context);

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return arraylist.size();

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

            final Holdler holder = new Holdler();
            convertView = inflater.inflate(R.layout.customview_cardtype, null);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_crdtype);
            holder.tv.setText(arrayofcardtype.get(position).getCard_name());

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {


                    for(int i=0;i<arrayofcardtype.size();i++) {

                        if(i==position){

                            Log.e("in if====","ok");

                            arrayofcardtype.get(i).setStatus(true);
                    // holder.img_check.setVisibility(View.VISIBLE);

                        }
                        else {

                            Log.e("in else====","ok...");
                            arrayofcardtype.get(i).setStatus(false);
                        }
                        text = arrayofcardtype.get(position).getCard_name();
                        Log.e("value---",""+text);
                        notifyDataSetChanged();
                    }


//                    if(listview.isItemChecked(position)) {
//
//                        text = cardtypedata[position];
//
//                        //String str=listview.getItemAtPosition(arg2).toString();
//
//                    }
                }
            });
            notifyDataSetChanged();

            Log.e("arrayofcardtype.get(position).getStatus()"," "+arrayofcardtype.get(position).getStatus());

            if (arrayofcardtype.get(position).getStatus()==true) {

                Log.e("in if---","in if----");
                holder.img_check.setVisibility(View.VISIBLE);
            } else {

                Log.e("in else---","in else----");
                holder.img_check.setVisibility(View.GONE);
            }


            return convertView;


        }
        class Holdler
        {
            ImageView img_check;
            TextView tv;
        }
    }


}
