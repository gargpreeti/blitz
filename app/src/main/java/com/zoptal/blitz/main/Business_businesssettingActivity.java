package com.zoptal.blitz.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.common.GooglePlacesAutocompleteAdapter;
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.model.Card_type;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Business_businesssettingActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener  {

    private ImageView img_backarrow, box1, box2,box3, box4, box5, box6;
    private Button btn_submit,btn_inform;
    private TextView tv_disable_payment,tv_disable_info;
    private RelativeLayout btn_information, btn_payment;
    private EditText ed_companyinfo,ed_phoneno,ed_email,ed_email1,ed_desc;
    Spinner ed_retails;
    AutoCompleteTextView ed_address;

    LinearLayout linear_paymnt, linear_companyinfo;
    private Bitmap bitmap1=null,bitmap2=null,bitmap3=null,bitmap4=null,bitmap5=null,bitmap6=null;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String access_tokn,img1="",img2="",img3="",img4="",img5="",img6="";
    Dialog dialog;
    String text;

    private EditText ed_fname,ed_lname,ed_cardno,ed_mnth,ed_exp,ed_cvvcode,ed_cardtype;
    private AutoCompleteTextView ed_billaddress;
    private Button btn_submitpaymnt;
    private CustomListAdapterCardtype adapter;
    //public static String [] cardtypedata={"visa","MasterCard","American Express"};
    ArrayList<Card_type> arrayofcardtype = new ArrayList<Card_type>();
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Spinner list_mnth;
    ListView  listview;
    String selectedcategory="";
    String expmnth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business__businesssetting);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        access_tokn = sharedpreferences1.getString("access_token", "");

        initview();
    }

    public void initview() {


        arrayofcardtype.add(new Card_type("visa",false));
        arrayofcardtype.add(new Card_type("mastercard",false));
        arrayofcardtype.add(new Card_type("american express",false));

        ed_companyinfo=(EditText)findViewById(R.id.ed_companyinfo);
        ed_retails=(Spinner)findViewById(R.id.ed_retails);
        ed_address=(AutoCompleteTextView)findViewById(R.id.ed_address);
        ed_phoneno=(EditText)findViewById(R.id.ed_phoneno);
        ed_email=(EditText)findViewById(R.id.ed_email);
        ed_email1=(EditText)findViewById(R.id.ed_email1);
        ed_desc=(EditText)findViewById(R.id.ed_desc);

        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_inform=(Button)findViewById(R.id.btn_inform);

        ed_address.setAdapter(new GooglePlacesAutocompleteAdapter(Business_businesssettingActivity.this, R.layout.list_item));

     //   String[] items = new String[]{"Restaurant","Bars/Clubs","Retail","Things To Do","Tickets&Events"};

        ArrayList<String> listcat = new ArrayList<String>();
        listcat.add("Restaurant");
        listcat.add("Bars/Clubs");
        listcat.add("Retail");
        listcat.add("Things To Do");
        listcat.add("Tickets&Events");


        List catlist = new ArrayList(new LinkedHashSet(listcat));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Business_businesssettingActivity.this, android.R.layout.simple_spinner_dropdown_item, catlist);

        ed_retails.setAdapter(adapter);

        ed_retails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                ((TextView) ed_retails.getSelectedView()).setTextColor(getResources().getColor(R.color.colorblack));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ed_fname=(EditText)findViewById(R.id.ed_fname);
        ed_lname=(EditText)findViewById(R.id.ed_lname);
        ed_billaddress=(AutoCompleteTextView)findViewById(R.id.ed_billaddress);
        ed_cardno=(EditText)findViewById(R.id.ed_cardno);
        ed_mnth=(EditText)findViewById(R.id.ed_mnth);
        ed_exp=(EditText)findViewById(R.id.ed_exp);
        list_mnth=(Spinner)findViewById(R.id.list_mnth);
        ed_cvvcode=(EditText)findViewById(R.id.ed_cvvcode);
        ed_cardtype=(EditText)findViewById(R.id.ed_cardtype);
        btn_submitpaymnt=(Button)findViewById(R.id.btn_submitpaymnt);

        ed_billaddress.setAdapter(new GooglePlacesAutocompleteAdapter(Business_businesssettingActivity.this, R.layout.list_item));

        ed_mnth.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_mnth.getWindowToken(), 0);

        ed_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_mnth.setVisibility(View.INVISIBLE);
                list_mnth.setVisibility(View.VISIBLE);
            }
        });

        String[] items_mnth = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Business_businesssettingActivity.this, android.R.layout.simple_spinner_dropdown_item, items_mnth);
        list_mnth.setAdapter(adapter1);
        //   list_mnth.setPrompt("Exp.[MM]");
        list_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // ed_exp.setText(list_mnth.getSelectedItem().toString().trim());
          //      ((TextView) list_mnth.getSelectedView()).setTextColor(getResources().getColor(R.color.colorblack));
               expmnth=list_mnth.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ed_exp.setInputType(InputType.TYPE_NULL);
        ed_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showYearDialog();
            }
        });

        img_backarrow = (ImageView) findViewById(R.id.img_backarrow);
        btn_information = (RelativeLayout) findViewById(R.id.btn_information);
        btn_payment = (RelativeLayout) findViewById(R.id.btn_payment);
        linear_paymnt = (LinearLayout) findViewById(R.id.linear_paymnt);
        linear_companyinfo = (LinearLayout) findViewById(R.id.linear_companyinfo);
        tv_disable_payment=(TextView)findViewById(R.id.tv_disable_payment);
        tv_disable_info=(TextView)findViewById(R.id.tv_disable_info);
        box1 = (ImageView) findViewById(R.id.box1);
        box2 = (ImageView) findViewById(R.id.box2);
        box3 = (ImageView) findViewById(R.id.box3);
        box4 = (ImageView) findViewById(R.id.box4);
        box5 = (ImageView) findViewById(R.id.box5);
        box6 = (ImageView) findViewById(R.id.box6);

        img_backarrow.setOnClickListener(this);
        btn_information.setOnClickListener(this);
        btn_payment.setOnClickListener(this);
        tv_disable_payment.setOnClickListener(this);
        tv_disable_info.setOnClickListener(this);
        box1.setOnClickListener(this);
        box2.setOnClickListener(this);
        box3.setOnClickListener(this);
        box4.setOnClickListener(this);
        box5.setOnClickListener(this);
        box6.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_submitpaymnt.setOnClickListener(this);
        btn_inform.setOnClickListener(this);

        if (NetworkConnection.isConnectedToInternet(Business_businesssettingActivity.this)) {
            get_business_info();
        }
        else {
            Toast.makeText(Business_businesssettingActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
            return;

        }
        ed_cardtype.setInputType(InputType.TYPE_NULL);
        ed_cardtype.setFocusable(false);
        ed_cardtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cardtype();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_businesssettingActivity.this, Business_AccountsettingActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_information:

                btn_information.setVisibility(View.VISIBLE);
                tv_disable_info.setVisibility(View.GONE);
                btn_payment.setVisibility(View.GONE);
                tv_disable_payment.setVisibility(View.VISIBLE);


                linear_companyinfo.setVisibility(View.VISIBLE);
                linear_paymnt.setVisibility(View.GONE);
                break;

            case R.id.btn_payment:

                btn_information.setVisibility(View.GONE);
                tv_disable_info.setVisibility(View.VISIBLE);
                btn_payment.setVisibility(View.VISIBLE);
                tv_disable_payment.setVisibility(View.GONE);
                linear_paymnt.setVisibility(View.VISIBLE);
                linear_companyinfo.setVisibility(View.GONE);
                break;


            case R.id.tv_disable_info:

                btn_information.setVisibility(View.VISIBLE);
                tv_disable_info.setVisibility(View.GONE);
                btn_payment.setVisibility(View.GONE);
                tv_disable_payment.setVisibility(View.VISIBLE);


                linear_companyinfo.setVisibility(View.VISIBLE);
                linear_paymnt.setVisibility(View.GONE);
                break;
            case R.id.tv_disable_payment:

                btn_information.setVisibility(View.GONE);
                tv_disable_info.setVisibility(View.VISIBLE);
                btn_payment.setVisibility(View.VISIBLE);
                tv_disable_payment.setVisibility(View.GONE);
                linear_paymnt.setVisibility(View.VISIBLE);
                linear_companyinfo.setVisibility(View.GONE);
                break;

            case R.id.box1:

                Crop.pickImage(Business_businesssettingActivity.this, 1);

                break;
            case R.id.box2:

                Crop.pickImage(Business_businesssettingActivity.this, 2);

                break;
            case R.id.box3:

                Crop.pickImage(Business_businesssettingActivity.this,3);

                break;

            case R.id.box4:

                Crop.pickImage(Business_businesssettingActivity.this,4);
                break;

            case R.id.box5:

                Crop.pickImage(Business_businesssettingActivity.this,5);
                break;

            case R.id.box6:

                Crop.pickImage(Business_businesssettingActivity.this,6);
                break;


            case R.id.btn_inform:

                Toast.makeText(Business_businesssettingActivity.this,"Direct link within your website to feature you promo item.ex.Restaurent.com/menu", Toast.LENGTH_LONG).show();

                break;


            case R.id.btn_submit:

                if (NetworkConnection.isConnectedToInternet(Business_businesssettingActivity.this)) {

                    add_business_info();

                }
                else {
                    Toast.makeText(Business_businesssettingActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                break;

            case R.id.btn_submitpaymnt:

                if (NetworkConnection.isConnectedToInternet(Business_businesssettingActivity.this)) {

                    payment_save_card();

                }
                else {

                    Toast.makeText(Business_businesssettingActivity.this,"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;

        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this, 1);

    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box1.setImageURI(null);
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box1.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void beginCrop2(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this, 2);

    }

    private void handleCrop2(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box2.setImageURI(null);
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box2.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void beginCrop3(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this, 3);

    }

    private void handleCrop3(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box3.setImageURI(null);
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box3.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void beginCrop4(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this,4);

    }

    private void handleCrop4(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box4.setImageURI(null);
            try {
                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box4.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private void beginCrop5(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this,5);

    }

    private void handleCrop5(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box5.setImageURI(null);
            try {
                bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box5.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    private void beginCrop6(Uri source) {
        Uri destination = Uri.fromFile(new File(Business_businesssettingActivity.this.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Business_businesssettingActivity.this,6);

    }

    private void handleCrop6(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            box6.setImageURI(null);
            try {
                bitmap6 = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            box6.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Business_businesssettingActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            onActivityResult1(requestCode, resultCode, data);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {

            onActivityResult2(requestCode, resultCode, data);
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {

            onActivityResult3(requestCode, resultCode, data);
        }

        if (requestCode ==4 && resultCode == RESULT_OK) {

            onActivityResult4(requestCode, resultCode, data);
        }

        if (requestCode ==5 && resultCode == RESULT_OK) {

            onActivityResult5(requestCode, resultCode, data);
        }

        if (requestCode ==6 && resultCode == RESULT_OK) {

            onActivityResult6(requestCode, resultCode, data);
        }
    }

    public void onActivityResult1(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == 1) {
                beginCrop(data.getData());

            }
            if (requestCode == 1) {

                handleCrop(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public void onActivityResult2(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==2) {
                beginCrop2(data.getData());

            }
            if (requestCode == 2) {

                handleCrop2(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public void onActivityResult3(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==3) {
                beginCrop3(data.getData());

            }
            if (requestCode == 3) {

                handleCrop3(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public void onActivityResult4(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==4) {
                beginCrop4(data.getData());

            }
            if (requestCode == 4) {

                handleCrop4(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public void onActivityResult5(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==5) {
                beginCrop5(data.getData());

            }
            if (requestCode == 5) {

                handleCrop5(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public void onActivityResult6(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==6) {
                beginCrop6(data.getData());

            }
            if (requestCode == 6) {

                handleCrop6(resultCode, data);
            }

        } catch (Exception e) {

        }
    }

    public String getStringImage1(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void add_business_info(){

        final String companyinfo = ed_companyinfo.getText().toString().trim();
        final String retail = ed_retails.getSelectedItem().toString().trim();
        final String address= ed_address.getText().toString().trim();
        final String phoneno = ed_phoneno.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();
        final String email1 = ed_email1.getText().toString().trim();
        final String desc = ed_desc.getText().toString().trim();


        if(companyinfo.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businesssettingActivity.this, "Please enter company information.");

            alertmsg.dialog.show();

            return;
        }

        if(address.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businesssettingActivity.this, "Please fill address.");

            alertmsg.dialog.show();

            return;
        }

     if(phoneno.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businesssettingActivity.this, "Please enter phone no.");

            alertmsg.dialog.show();

            return;
        }
        if(desc.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(Business_businesssettingActivity.this, "Please enter description.");

            alertmsg.dialog.show();

            return;
        }

        Log.e("bitmap1===",""+bitmap1);
        if(bitmap1!=null) {
         img1 = getStringImage1(bitmap1);
        }
        Log.e("bitmap2===",""+bitmap2);
        if(bitmap2!=null){
            img2= getStringImage1(bitmap2);
               }
        if(bitmap3!=null) {
             img3 = getStringImage1(bitmap3);
        }
        if(bitmap4!=null) {
            img4 = getStringImage1(bitmap4);
        }
        if(bitmap5!=null) {
           img5 = getStringImage1(bitmap5);
        }
        if(bitmap6!=null) {
             img6 = getStringImage1(bitmap6);
        }



        loading = new ProgressDialog(Business_businesssettingActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.add_business_info,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg=main_obj.getString("message");

                            Toast.makeText(Business_businesssettingActivity.this,msg,Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_businesssettingActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("company_name",companyinfo);
                params.put("company_category",retail);
                params.put("company_address1", address);
             //   params.put("company_address2", cpw);
                params.put("company_phone_number", phoneno);
                params.put("company_website", email);
                params.put("company_special_website", email1);
                params.put("description", desc);
                params.put("type", "android");
                Log.e("img1----",""+img1);
                params.put("image1",img1 );
                params.put("image2",img2);
                params.put("image3", img3);
                params.put("image4", img4);
                params.put("image5",img5);
                params.put("image6",img6);
                params.put("access_token", access_tokn);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void get_business_info() {

        loading = new ProgressDialog(Business_businesssettingActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.get_business_info,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("message");

                            String company_name  =obj.getString("company_name");
                            String compny_cat =obj.getString("company_category");
                            String  address =obj.getString("company_address1");
                            String  compny_website =obj.getString("company_website");
                            String  phoneno =obj.getString("company_phone_number");
                            String  desc =obj.getString("description");
                            String special_website=obj.getString("company_special_website");
                            String  img1 =obj.getString("image1");
                            String  img2 =obj.getString("image2");
                            String  img3 =obj.getString("image3");
                            String  img4 =obj.getString("image4");
                            String  img5 =obj.getString("image5");
                            String  img6 =obj.getString("image6");


                            ed_companyinfo.setText(company_name);
                       //     ed_retails.set
                            ed_address.setText(address);
                            ed_phoneno.setText(phoneno);
                            ed_email.setText(compny_website);
                            ed_email1.setText(special_website);
                            ed_desc.setText(desc);
                            selectedcategory=compny_cat;

//                            String[] items = new String[]{selectedcategory,"Restaurant","Bars/Clubs","Retail","Things To Do","Tickets&Events"};
//
//                            Set<String> uniqueWords = new HashSet<String>(Arrays.asList(items));

                            ArrayList<String> listcat = new ArrayList<String>();
                            listcat.add(selectedcategory);
                            listcat.add("Restaurant");
                            listcat.add("Bars/Clubs");
                            listcat.add("Retail");
                            listcat.add("Things To Do");
                            listcat.add("Tickets&Events");


                            List catlist = new ArrayList(new LinkedHashSet(listcat));

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Business_businesssettingActivity.this, android.R.layout.simple_spinner_dropdown_item, catlist);

                            ed_retails.setAdapter(adapter);

                            if(img1.isEmpty()){
                            }
                            else{

                          Picasso.with(Business_businesssettingActivity.this).load(img1).into(box1);

                            }
                            if(img2.isEmpty()){
                            }
                            else{
                                Picasso.with(Business_businesssettingActivity.this).load(img2).into(box2);

                            }
                            if(img3.isEmpty()){
                            }
                            else{
                                Picasso.with(Business_businesssettingActivity.this).load(img3).into(box3);

                            }
                            if(img4.isEmpty()){
                            }
                            else{
                                Picasso.with(Business_businesssettingActivity.this).load(img4).into(box4);

                            }
                            if(img5.isEmpty()){
                            }
                            else{
                                Picasso.with(Business_businesssettingActivity.this).load(img5).into(box5);

                            }
                            if(img6.isEmpty()){
                            }
                            else{
                                Picasso.with(Business_businesssettingActivity.this).load(img6).into(box6);

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
                        Toast.makeText(Business_businesssettingActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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



    private void payment_save_card(){

        final String firstname = ed_fname.getText().toString().trim();
        final String lastname = ed_lname.getText().toString().trim();
        final String billingaddress= ed_billaddress.getText().toString().trim();
        final String crdno = ed_cardno.getText().toString().trim();
     //   final String expmnth = ed_mnth.getText().toString().trim();
        final String expyr = ed_exp.getText().toString().trim();
        final String code_cvv = ed_cvvcode.getText().toString().trim();
        final String crdtype = ed_cardtype.getText().toString().trim();


        loading = new ProgressDialog(Business_businesssettingActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.save_credit_card,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            String msg=main_obj.getString("message");

                            Toast.makeText(Business_businesssettingActivity.this,msg,Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                     //   Toast.makeText(Business_businesssettingActivity.this,msg,Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Business_businesssettingActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("cc_fname",firstname);
                params.put("cc_lname",lastname);
                params.put("address", billingaddress);
                params.put("cc_number", crdno);
                params.put("cvv", code_cvv);
                params.put("exp_month", expmnth);
                params.put("exp_year", expyr);
                params.put("card_type", crdtype);
                params.put("access_token", access_tokn);

                Log.e("paymnt parameter",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);

    }

    public void dialog_cardtype() {

//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent) {
//            @Override
//            public boolean onTouchEvent(MotionEvent event) {
//                // Tap anywhere to close dialog.
//                dialog.dismiss();
//                return true;
//            }
//        };
        dialog = new Dialog(Business_businesssettingActivity.this, android.R.style.Theme_Translucent);
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

        adapter = new CustomListAdapterCardtype(Business_businesssettingActivity.this,arrayofcardtype);
        listview.setAdapter(adapter);

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//
//                if(listview.isItemChecked(position)) {
//
//              text = cardtypedata[position];
//
//                    //String str=listview.getItemAtPosition(arg2).toString();
//             Log.e("value---",""+text);
//                }
//            }
//        });

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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_exp.getWindowToken(), 0);

        final Dialog d = new Dialog(Business_businesssettingActivity.this);
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
                ed_exp.setText(String.valueOf(nopicker.getValue()));
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



