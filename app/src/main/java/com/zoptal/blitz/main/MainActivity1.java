package com.zoptal.blitz.main;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zoptal.blitz.fragment.Fragment_BuyerAccountsetting;
import com.zoptal.blitz.fragment.Fragment_LocMap;
import com.zoptal.blitz.url.RegisterUrl;

import java.util.HashMap;
import java.util.Map;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    String TITLES[] = {"RESTAURANT", "BARS/CLUBS", "RETAIL", "THINGS TO DO", "TICKETS & EVENT", "FAVOURITIES", "ACCOUNT SETTINGS","NOTIFICATIONS"};
    int ICONS[] = {R.mipmap.restaurant_icon, R.mipmap.barsclubs, R.mipmap.retails, R.mipmap.things_to_do, R.mipmap.tickets_event, R.mipmap.favourites, R.mipmap.account_settings,R.mipmap.notification};

    public TextView tv_bck, tv_login;
    public TextView textToolHeader;
    public EditText ed_srch;
    public ImageView img_dltall;
    public ImageView img_srch,img_menubar,img_logo,img_star,img_cross;
    public ImageView img_user;
    public TextView tv_main, tv_name,tv_logout,tv_usrname,tv_promocode;
    Toolbar toolbar;                              // Declaring the Toolbar Object
    public  Drawable drawable;
    public static RecyclerView mRecyclerView;                           // Declaring RecyclerView

    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager

    public static DrawerLayout Drawer;
    public static ActionBarDrawerToggle mDrawerToggle;
    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token,buyer_name,buyer_promo_code,buyer_profile_pic;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tv_bck = (TextView) findViewById(R.id.bck);
        tv_bck.setVisibility(View.INVISIBLE);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_logout=(TextView)findViewById(R.id.tv_logout);
        tv_usrname=(TextView)findViewById(R.id.tv_usrname);
        tv_promocode=(TextView)findViewById(R.id.tv_promocode);
        img_user=(ImageView) findViewById(R.id.img_user);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        tv_login.setOnClickListener(this);
        img_logo.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        textToolHeader = (TextView) toolbar.findViewById(R.id.textView3);
        textToolHeader.setText("Offers");
        setSupportActionBar(toolbar);
        textToolHeader.setVisibility(View.VISIBLE);
        img_srch=(ImageView)toolbar.findViewById(R.id.img_srch);
        ed_srch=(EditText) toolbar.findViewById(R.id.ed_srch);
        img_cross=(ImageView)toolbar.findViewById(R.id.img_cross);
        img_star=(ImageView)toolbar.findViewById(R.id.img_star);
        img_dltall=(ImageView)toolbar.findViewById(R.id.img_dltall);
        img_menubar=(ImageView)toolbar.findViewById(R.id.img_menubar);
        img_srch.setOnClickListener(this);
        img_cross.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        mAdapter = new MyAdapter(MainActivity1.this, TITLES, ICONS);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);

        // Setting the adapter to RecyclerView
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.e("open toggle ===","closed");
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.e("close toggle ===","closed");
                // Code here will execute once drawer is closed
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {

                if (item != null && item.getItemId() == android.R.id.home) {
                    if (Drawer.isDrawerOpen(Gravity.RIGHT)) {
                        Drawer.closeDrawer(Gravity.RIGHT);
                        Log.e("close ===","closed");
                        img_menubar.setVisibility(View.GONE);
                    } else {
                        Log.e("open ===","opened");
                        Drawer.openDrawer(Gravity.RIGHT);
                        img_menubar.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.menu_bar, getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Drawer.isDrawerVisible(GravityCompat.START)) {
                    Drawer.closeDrawer(GravityCompat.START);
                } else {
                    Drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        mDrawerToggle.setHomeAsUpIndicator(drawable);

//        if(Drawer.isDrawerOpen(GravityCompat.START)) {
//            Log.e("close ===","closed");
//            img_menubar.setVisibility(View.GONE);
//            Drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
//        }else{
//            Log.e("open ===","opened");
//            Drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
//            img_menubar.setVisibility(View.VISIBLE);
//        }

        sharedpreferences1 =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");
        buyer_name = sharedpreferences1.getString("buyer_name", "");
        buyer_promo_code = sharedpreferences1.getString("buyer_promo_code", "");
        buyer_profile_pic = sharedpreferences1.getString("buyer_profile_pic", "");

        tv_usrname.setText(buyer_name);
        tv_promocode.setText(buyer_promo_code);
        if(buyer_profile_pic.isEmpty()){

        }
        else {
            Picasso.with(MainActivity1.this).load(buyer_profile_pic).into(img_user);
        }
        Log.e("buyer access tokn---",""+buyer_access_token);

       if(buyer_access_token.isEmpty()){

          tv_logout.setVisibility(View.GONE);
           tv_login.setVisibility(View.VISIBLE);
           img_user.setVisibility(View.GONE);
           tv_usrname.setVisibility(View.GONE);
           tv_promocode.setVisibility(View.GONE);
       }
        else {
           tv_logout.setVisibility(View.VISIBLE);
           tv_login.setVisibility(View.GONE);
           img_user.setVisibility(View.VISIBLE);
           tv_usrname.setVisibility(View.VISIBLE);
           tv_promocode.setVisibility(View.VISIBLE);
       }

        tv_logout.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == 1 && resultCode == RESULT_OK) {
//
//          MyAdapter.fragment_buyerAccountsetting.onActivityResult1(requestCode, resultCode, data);
//
//        }
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Fragment_BuyerAccountsetting.fragment_buyerAccount.onActivityResult1(requestCode, resultCode, data);

        }

    }
//    @Override
//    public void onRestart() {
//        super.onRestart();
//        Log.e("mainactivity1----","restart");
//        finish();
//        startActivity(getIntent());
//        //When BACK BUTTON is pressed, the activity on the stack is restarted
//        //Do what you want on the refresh procedure here
//    }

    @Override
    public void onBackPressed() {

        Log.e("mainactivity1----","backpressed");
//        if (getFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();
//        } else {
//            getFragmentManager().popBackStack();
//        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_login:

                Intent i = new Intent(MainActivity1.this,Buyer_SigninActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.img_logo:

                MyAdapter.itemvalue=0;
                textToolHeader.setText("Offers");
                Fragment fragment3=new Fragment_LocMap();
                FragmentManager fragmentManager2 =getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.activity_main_content_fragment, fragment3).commit();
                Drawer.closeDrawer(Gravity.LEFT);

                break;

            case R.id.img_srch:

              ed_srch.setVisibility(View.VISIBLE);
              textToolHeader.setVisibility(View.GONE);
                img_cross.setVisibility(View.VISIBLE);
                img_srch.setVisibility(View.GONE);
                break;

            case R.id.img_cross:
                ed_srch.setVisibility(View.GONE);
                textToolHeader.setVisibility(View.VISIBLE);
                img_srch.setVisibility(View.VISIBLE);
                img_cross.setVisibility(View.GONE);
                break;

            case R.id.tv_logout:

                dialoglogout();
                break;
        }
    }

    public void dialoglogout() {


   final  Dialog  dialog = new Dialog(MainActivity1.this, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_buyer_logout);

        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
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

                userLogout();
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

    private void userLogout() {

        loading = new ProgressDialog(MainActivity1.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.logout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(MainActivity1.this," Logout Successfully",Toast.LENGTH_SHORT).show();

                        Log.e("respose===",""+response);


                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("buyer_id","");
                        editor1.putString("buyer_access_token","");
                        editor1.commit();

                        Intent i = new Intent(MainActivity1.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity1.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
