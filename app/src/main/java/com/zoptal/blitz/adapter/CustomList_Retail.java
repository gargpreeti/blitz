package com.zoptal.blitz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoptal.blitz.R;
import com.zoptal.blitz.main.Activity_ListDetail;
import com.zoptal.blitz.model.HomeFeeds_list;

import java.util.List;

/**
 * Created by zotal.102 on 23/03/17.
 */
public class CustomList_Retail extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    //  String [] name;
    //String[] headingname;
    private List<HomeFeeds_list> homefeed;

    public CustomList_Retail(Context context,List<HomeFeeds_list> homefeed) {

        this.context = context;
        this.homefeed=homefeed;
        inflater = LayoutInflater.from(context);

        // TODO Auto-generated constructor stub
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub


        return homefeed.size();

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

        Holdlerretail holder = new Holdlerretail();
        convertView = inflater.inflate(R.layout.customview_offers, null);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_headingname = (TextView) convertView.findViewById(R.id.tv_headingname);
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        holder.tv_colr = (TextView) convertView.findViewById(R.id.tv_colr);
        holder.relative_list=(RelativeLayout)convertView.findViewById(R.id.relative_list);

        holder.tv_name.setText(homefeed.get(position).getBusiness_name());
        holder.tv_headingname.setText(homefeed.get(position).getSales_heading());
        holder.tv_colr.setBackgroundColor(Color.parseColor("#e79999"));


        holder.relative_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,Activity_ListDetail.class);
                context.startActivity(i);

            }
        });


        return convertView;


    }

    class Holdlerretail
    {
        TextView tv_name;
        TextView tv_headingname;
        TextView tv_time;
        TextView tv_colr;
        RelativeLayout relative_list;
    }
}



