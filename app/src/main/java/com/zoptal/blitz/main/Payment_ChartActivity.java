package com.zoptal.blitz.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zoptal.blitz.model.Chart_list;
import com.zoptal.blitz.url.RegisterUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment_ChartActivity extends AppCompatActivity {

    private ImageView img_backarrow;
//    TableLayout tl;
    ListView listview;
    ProgressDialog loading;
    CustomListAdapter adapter;
    private List<Chart_list> chrtList = new ArrayList<Chart_list>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__chart);

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);

        img_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

//     tl = (TableLayout) findViewById(R.id.main_table);
//        get_paymnt_chart();

        listview=(ListView)findViewById(R.id.listview);


        adapter = new CustomListAdapter(chrtList);
        listview.setAdapter(adapter);

        get_paymnt_chart();
    }






    private void get_paymnt_chart() {

        loading = new ProgressDialog(Payment_ChartActivity.this,R.style.AppCompatAlertDialogStyle);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl.payment_chart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("respose===",""+response);


                        try{
                            JSONObject main_obj = new JSONObject(response);
                            JSONArray ary_store= main_obj.getJSONArray("data");

                            for (int i = 0; i < ary_store.length(); i++) {

                                JSONObject obj = ary_store.getJSONObject(i);

                                Chart_list storelistdata = new Chart_list();

                                storelistdata.setRange(obj.getString("range"));
                                storelistdata.setAmount_per_sale(obj.getString("amount_per_sale"));
                                storelistdata.setCommission_per_sale(obj.getString("commission_per_sale"));
                                storelistdata.setExtra_bonus(obj.getString("extra_bonus"));

// adding storelistdata to storeList array
                                chrtList.add(storelistdata);

                            }








//                            TableLayout tv=(TableLayout) findViewById(R.id.main_table);
//                           // tv.removeAllViewsInLayout();
//                            int flag=1;
//
//                            // when i=-1, loop will display heading of each column
//                            // then usually data will be display from i=0 to jArray.length()
//                            for(int i=-1;i<ary_store.length();i++){
//
//                                TableRow tr=new TableRow(Payment_ChartActivity.this);
//
//                                tr.setLayoutParams(new TableLayout.LayoutParams(
//                                        TableLayout.LayoutParams.FILL_PARENT,
//                                        TableLayout.LayoutParams.WRAP_CONTENT));
//
//                                // this will be executed once
//                                if(flag==1){
//
////                                    TextView b3=new TextView(Payment_ChartActivity.this);
////                                    b3.setText("Range");
////                                    b3.setPadding(10,30,0,30);
////                                    b3.setTextColor(Color.BLACK);
////                                    b3.setBackgroundColor(Color.parseColor("#dcdcdc"));
////                                    b3.setTextSize(12);
////                                    tr.addView(b3);
////
////                                    TextView b4=new TextView(Payment_ChartActivity.this);
////                                    b4.setPadding(0, 30, 0,30);
////                                    b4.setTextSize(12);
////                                    b4.setText("Referral Commission");
////                                    b4.setBackgroundColor(Color.parseColor("#dcdcdc"));
////                                    b4.setTextColor(Color.BLACK);
////                                    tr.addView(b4);
////
////                                    TextView b5=new TextView(Payment_ChartActivity.this);
////                                    b5.setPadding(5, 30, 0, 30);
////                                    b5.setText("Revenue per post");
////                                    b5.setBackgroundColor(Color.parseColor("#dcdcdc"));
////                                    b5.setTextColor(Color.BLACK);
////                                    b5.setTextSize(12);
////                                    tr.addView(b5);
////
////
////                                    TextView b6=new TextView(Payment_ChartActivity.this);
////                                    b6.setPadding(5, 30, 0, 30);
////                                    b6.setText("Range Entry Bonus");
////                                    b6.setBackgroundColor(Color.parseColor("#dcdcdc"));
////                                    b6.setTextColor(Color.BLACK);
////                                    b6.setTextSize(12);
////                                    tr.addView(b6);
////                                    tv.addView(tr);
////
////                                    final View vline = new View(Payment_ChartActivity.this);
////                                    vline.setLayoutParams(new
////                                            TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
////                                    vline.setBackgroundColor(Color.BLUE);
////                                    tv.addView(vline); // add line below heading
//                                    flag=0;
//                                }
//                                else {
//                                    JSONObject json_data = ary_store.getJSONObject(i);
//
//                                    TextView b=new TextView(Payment_ChartActivity.this);
//                                    String str=String.valueOf(json_data.getString("range"));
//                                    b.setText(str);
//                                    b.setPadding(10,20,10,20);
//                                    b.setTextColor(Color.GRAY);
//                                    b.setTextSize(15);
//                                    tr.addView(b);
//
//                                    TextView b1=new TextView(Payment_ChartActivity.this);
//                                    b1.setPadding(80, 20, 0, 0);
//                                    b1.setTextSize(15);
//                                    String str1=json_data.getString("amount_per_sale");
//                                    b1.setText(str1);
//                                    b.setTextColor(Color.GRAY);
//                                    tr.addView(b1);
//
//                                    TextView b2=new TextView(Payment_ChartActivity.this);
//                                    b2.setPadding(10, 20, 0,0);
//                                    String str2=String.valueOf(json_data.getString("commission_per_sale"));
//                                    b2.setText(str2);
//                                    b.setTextColor(Color.GRAY);
//                                    b2.setTextSize(15);
//                                    tr.addView(b2);
//                                  //  tv.addView(tr);
//
//
//                                    TextView b3=new TextView(Payment_ChartActivity.this);
//                                    b3.setPadding(0, 20, 20, 20);
//                                    String str3=String.valueOf(json_data.getString("extra_bonus"));
//                                    b3.setText(str3);
//                                    b.setTextColor(Color.GRAY);
//                                    b3.setTextSize(15);
//                                    tr.addView(b3);
//                                    tv.addView(tr);
//
//                                    final View vline1 = new View(Payment_ChartActivity.this);
//                                    vline1.setLayoutParams(new
//                                            TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
//                                    vline1.setBackgroundColor(Color.WHITE);
//                                    tv.addView(vline1);  // add line below each row
                               // }
                           // }
                        }


                        catch(JSONException e){
                            Log.e("log_tag", "Error parsing data " + e.toString());
                            Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Payment_ChartActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();


                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    class CustomListAdapter extends BaseAdapter {

        LayoutInflater inflater;
        Context context;
        // String[] historyname;

        private List<Chart_list> chartItems;

        public CustomListAdapter(List<Chart_list> chartItems) {

            this.context = context;
            this.chartItems = chartItems;
            inflater = LayoutInflater.from(getApplicationContext());

            // TODO Auto-generated constructor stub
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub

                 return chartItems.size();

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

            Holdlerchart holder = new Holdlerchart();
            convertView = inflater.inflate(R.layout.chart_test, null);

            holder.tv_range = (TextView) convertView.findViewById(R.id.tv_range);
            holder.tv_refer = (TextView) convertView.findViewById(R.id.tv_refer);
            holder.tv_revenue = (TextView) convertView.findViewById(R.id.tv_revenue);
            holder.tv_bonus = (TextView) convertView.findViewById(R.id.tv_bonus);


            holder.tv_range.setText(chartItems.get(position).getRange());
            holder.tv_refer.setText(chartItems.get(position).getAmount_per_sale());
            holder.tv_revenue.setText(chartItems.get(position).getCommission_per_sale());
            holder.tv_bonus.setText(chartItems.get(position).getExtra_bonus());



            return convertView;

        }

    }
    class Holdlerchart
    {

        TextView tv_range;
        TextView tv_refer;
        TextView tv_revenue;
        TextView tv_bonus;

    }
}
