package com.zoptal.blitz.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zoptal.blitz.R;
import com.zoptal.blitz.main.MainActivity1;

import java.io.File;
import java.io.IOException;


public class Fragment_BuyerAccountsetting extends Fragment implements View.OnClickListener{

    private RelativeLayout relative_account,relative_changepw,relative_businessmarkting;
    View view;
    MainActivity1 activity1;
    public  ImageView user_img;
    private TextView tv_username;
    Bitmap bitmap;
    public static Fragment_BuyerAccount fragment_buyerAccount;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_name,buyer_profile_pic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

      view = inflater.inflate(R.layout.fragment_buyeraccountsetting, container, false);
      getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activity1=(MainActivity1)getActivity();
        activity1.textToolHeader.setText("Account Setting");
        activity1.drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.menu_bar, getActivity().getTheme());
        activity1.mDrawerToggle.setHomeAsUpIndicator(activity1.drawable);
        activity1.tv_bck.setVisibility(View.GONE);
        activity1.img_srch.setVisibility(View.INVISIBLE);
        activity1.img_star.setVisibility(View.GONE);
        activity1.img_dltall.setVisibility(View.GONE);


        initview();

        return view;

    }

    private void initview() {

        relative_account=(RelativeLayout)view.findViewById(R.id.relative_account);
        relative_changepw=(RelativeLayout)view.findViewById(R.id.relative_changepw);
        relative_businessmarkting=(RelativeLayout)view.findViewById(R.id.relative_businessmarkting);
        user_img=(ImageView)view.findViewById(R.id.user_img);
        tv_username=(TextView)view.findViewById(R.id.tv_username);

        relative_account.setOnClickListener(this);
        relative_changepw.setOnClickListener(this);
        relative_businessmarkting.setOnClickListener(this);
        user_img.setOnClickListener(this);

        sharedpreferences1 =getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_name = sharedpreferences1.getString("buyer_name", "");
        buyer_profile_pic = sharedpreferences1.getString("buyer_profile_pic", "");

        activity1.tv_usrname.setText(buyer_name);
        if(buyer_profile_pic.isEmpty()){

        }
        else {
            Picasso.with(getActivity()).load(buyer_profile_pic).into(activity1.img_user);
        }
        tv_username.setText(buyer_name);

        if(buyer_profile_pic.isEmpty()){

        }
        else {
            Picasso.with(getActivity()).load(buyer_profile_pic).into(user_img);
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.relative_account:

                fragment_buyerAccount = new Fragment_BuyerAccount();
                FragmentManager fragmentManager7 =getFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.activity_main_content_fragment, fragment_buyerAccount).commit();

                break;


            case R.id.relative_changepw:

                Fragment fragment_changepw = new Fragment_ChangePw();
                FragmentManager fragmentManager =getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, fragment_changepw).commit();

                break;

            case R.id.relative_businessmarkting:

                Fragment fragment_markting = new   Fragment_Markting();
                FragmentManager fragmentManager1 =getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.activity_main_content_fragment, fragment_markting).commit();
                break;

            case R.id.user_img:

              //  Crop.pickImage(getActivity(), 1);
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


}