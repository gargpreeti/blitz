package com.zoptal.blitz.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.zoptal.blitz.common.NetworkConnection;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Fragment_BuyerAccount extends Fragment implements View.OnClickListener {

    private EditText ed_name,ed_email;

    private Button btn_clear,btn_save;
    private ImageView img_cross;
    View view;
    public  ImageView user_img;
    Bitmap bitmap;
    ProgressDialog loading;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token,buyer_name,buyer_id,buyer_email,buyer_pic;
    String userimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment_buyer_account, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initview();

        return view;

    }

    private void initview() {

        img_cross = (ImageView) view.findViewById(R.id.img_cross);
        img_cross.setOnClickListener(this);
        user_img=(ImageView)view.findViewById(R.id.user_img);

        ed_name=(EditText)view.findViewById(R.id.ed_name);
        ed_email=(EditText)view.findViewById(R.id.ed_email);
        btn_save=(Button)view.findViewById(R.id.btn_save);
        btn_clear=(Button)view.findViewById(R.id.btn_clear);

        user_img.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        sharedpreferences1 =getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

        get_profile_info();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_cross:

                Fragment fragment7 = new Fragment_BuyerAccountsetting();
                FragmentManager fragmentManager7 =getFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.activity_main_content_fragment, fragment7).commit();

                break;

            case R.id.user_img:

                Crop.pickImage(getActivity(),1);

                break;

            case R.id.btn_clear:

                ed_name.setText("");
                ed_email.setText("");

                break;

            case R.id.btn_save:

                if (NetworkConnection.isConnectedToInternet(getActivity())) {
                   buyerprofileupdate();
                }
                else {
                    Toast.makeText(getActivity(),"Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                break;

        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity(),1);
    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode ==getActivity().RESULT_OK) {

            user_img.setImageURI(null);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            user_img.setImageURI(Crop.getOutput(result));
        }
        else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult1(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode ==1) {
                beginCrop(data.getData());

            }
            if (requestCode == 1) {

                handleCrop(resultCode, data);
            }

        } catch (Exception e) {

        }

    }

    private void get_profile_info() {

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.buyer_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose getprofiledata===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONObject  obj=main_obj.getJSONObject("data");

                            buyer_id  =obj.getString("id");
                            buyer_name =obj.getString("name");
                            buyer_email=obj.getString("email");
                            buyer_pic =obj.getString("profile_pic");

                             Log.e("buyername---",""+buyer_name);
                            Log.e("buyer_pic---",""+buyer_pic);
                            ed_name.setText(buyer_name);
                            ed_email.setText(buyer_email);
                            Picasso.with(getActivity()).load(buyer_pic).into(user_img);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void buyerprofileupdate(){

        final String name = ed_name.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();


        if(name.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(getActivity(), "Please enter your name.");

            alertmsg.dialog.show();
            return;
        }

        if(email.equals("")){

            AlertDialogMsg alertmsg = new
                    AlertDialogMsg(getActivity(), "Please enter your email.");

            alertmsg.dialog.show();
            return;
        }

        Log.e("bitmap value=====",""+bitmap);





try {
    if (bitmap == null) {
//            bitmap=((BitmapDrawable)user_img.getDrawable()).getBitmap();
//            userimg=getStringImage(bitmap);

        BitmapDrawable drawable = (BitmapDrawable) user_img.getDrawable();
        bitmap = drawable.getBitmap();
        userimg = getStringImage(bitmap);


        return;
    }
}catch (NullPointerException e){

}

        try {
            if (bitmap != null) {
                userimg = getStringImage(bitmap);
            }
        }
        catch (NullPointerException e){

        }

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.update_profile,
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
                                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {

                                String message =main_obj.getString("message");
                                JSONObject obj = main_obj.getJSONObject("data");
                                String buyer_name =obj.getString("name");
                                String buyer_profile_pic =obj.getString("profile_pic");


                                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                editor1.putString("buyer_name",buyer_name);
                                editor1.putString("buyer_profile_pic",buyer_profile_pic);
                                editor1.commit();


                                   Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

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
                        //  Toast.makeText(Business_AccountActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);

                if(userimg==null){
                    userimg="";
                }

                params.put("profile_pic",userimg);
                params.put("user_type", "0");
                params.put("access_token",buyer_access_token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}