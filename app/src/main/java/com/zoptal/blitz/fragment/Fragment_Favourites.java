package com.zoptal.blitz.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoptal.blitz.R;
import com.zoptal.blitz.main.MainActivity1;
import com.zoptal.blitz.model.Favourite_List;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Favourites extends Fragment  {

   MainActivity1 activity1;
    TextView tv_msg;
    ListView listview;
    View view;
    public static String [] listdata={"Dummy store","Dummy store","Dummy store"};
   Dialog dialog;

    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token;
    ProgressDialog loading;
    CustomListAdapterFav adapterfav;
    private List<Favourite_List> favList = new ArrayList<Favourite_List>();
    String fav_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

         view = inflater.inflate(R.layout.fragment_fav, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activity1 = (MainActivity1) getActivity();
        activity1.img_srch.setVisibility(View.INVISIBLE);
        activity1.img_star.setVisibility(View.GONE);
        activity1.img_dltall.setVisibility(View.GONE);


        sharedpreferences1 =getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");
        init();

        return view;

    }

    private void init() {

        listview=(ListView)view.findViewById(R.id.listview);
        tv_msg=(TextView)view.findViewById(R.id.tv_msg);
       // listview.setAdapter(new CustomListAdapterFav(favList));

        adapterfav = new CustomListAdapterFav(favList);
        listview.setAdapter(adapterfav);


        getfavlist();
    }

    class CustomListAdapterFav extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
       // String [] historyname;

        private List<Favourite_List> favItems;

        public CustomListAdapterFav(List<Favourite_List> favItems) {

            this.context = context;
            this.favItems=favItems;
            inflater = LayoutInflater.from(getActivity());

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            if(favItems.size()==0){
                tv_msg.setVisibility(View.VISIBLE);
            }
            else {
                tv_msg.setVisibility(View.GONE);
            }
            return favItems.size();

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

            Holdlerfav holder = new Holdlerfav();
            convertView = inflater.inflate(R.layout.customview_fav_buyer, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img_dlt);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_storename);
            holder.tv_address= (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_colr = (TextView) convertView.findViewById(R.id.tv_colr);
            holder.tv.setText(favItems.get(position).getCompanyname());
            holder.tv_address.setText(favItems.get(position).getStore_address());



            String cmpny_category=favItems.get(position).getCompanycategory();
            if(cmpny_category.equals("Restaurant")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#e09e6d"));
            }
            if(cmpny_category.equals("Bars/Clubs")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#dfaad1"));
            }

            if(cmpny_category.equals("Retail")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));
            }
            if(cmpny_category.equals("Things To Do")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#9ec780"));
            }
            if(cmpny_category.equals("Tickets&Events")){
                holder.tv_colr.setBackgroundColor(Color.parseColor("#A9C5EB"));
            }





            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fav_id=favItems.get(position).getId();
                    dialogdltconfirmation(position);

                }
            });
            return convertView;


        }

    }
    class Holdlerfav
    {
        ImageView img;
        TextView tv;
        TextView tv_address;
        TextView tv_colr;
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
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
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

                deletefav(pos);
                adapterfav.notifyDataSetChanged();

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


    private void getfavlist() {

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.my_fav,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose listttfav===",""+response);

                        try {
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Favourite_List favlistdata = new Favourite_List();

                                favlistdata.setId(obj.getString("id"));
                                favlistdata.setStore_id("store_id");
                                favlistdata.setStore_name(obj.getString("store_name"));
                                favlistdata.setCompanycategory(obj.getString("company_category"));
                                favlistdata.setStore_address(obj.getString("store_address"));
                                favlistdata.setStore_phone(obj.getString("store_phone"));
                                favlistdata.setCompanyname(obj.getString("company_name"));
                                favlistdata.setCompnyaddress(obj.getString("company_address"));
// adding storelistdata to storeList array
                                favList.add(favlistdata);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterfav.notifyDataSetChanged();
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

    private void deletefav(final int pos) {

        loading = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.remove_fav,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);
                        Log.e("posss",""+pos);
                        favList.remove(pos);
                        adapterfav.notifyDataSetChanged();
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
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                map.put("access_token",buyer_access_token);
                map.put("id",fav_id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
