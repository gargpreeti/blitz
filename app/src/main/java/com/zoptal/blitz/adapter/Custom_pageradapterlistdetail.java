package com.zoptal.blitz.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zoptal.blitz.R;


public class Custom_pageradapterlistdetail extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources = {
            R.drawable.noimage,
            R.drawable.noimage,
            R.drawable.noimage,
            R.drawable.noimage,

    };
    public Custom_pageradapterlistdetail(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

     return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);


        RelativeLayout imageView = (RelativeLayout) itemView.findViewById(R.id.imageView);


//        Log.e("ist img", "" + Json_ProductDetail1.img_array.get(0));
//        Log.e("2nd img", "" + Json_ProductDetail1.img_array.get(1));
//        Log.e("3rd img", "" + Json_ProductDetail1.img_array.get(2));
//        Log.e("4thimg", "" + Json_ProductDetail1.img_array.get(3));




//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(1)).into(imageView);
//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(2)).into(imageView);
//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(3)).into(imageView);
      //   imageView.setImageResource(mResources[position]);
        imageView.setBackgroundResource(mResources[position]);
//      try{
//        Picasso.with(mContext).load(Json_ProductDetail1.model_postAdData.getProduct_img()).into(imageView.ge);
//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(1)).into(imageView);
//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(2)).into(imageView);
//        Picasso.with(mContext).load(Json_ProductDetail1.img_array.get(3)).into(imageView);
//    }
//    catch (Exception e){}

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }



}
