package com.zoptal.blitz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoptal.blitz.R;

/**
 * Created by zotal.102 on 07/04/17.
 */
public class CustomListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    String [] cardtype;

    public CustomListAdapter(Context context,String[] cardtype) {

        this.context = context;
        this.cardtype=cardtype;
        inflater = LayoutInflater.from(context);

        // TODO Auto-generated constructor stub
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return cardtype.length;

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

        Holdler holder = new Holdler();
        convertView = inflater.inflate(R.layout.customview_cardtype, null);
        holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
        holder.tv = (TextView) convertView.findViewById(R.id.tv_crdtype);
        holder.tv.setText(cardtype[position]);



        return convertView;


    }
    class Holdler
    {
       ImageView img_check;
        TextView tv;
    }
}

