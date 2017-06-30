package com.zoptal.blitz.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zoptal.blitz.R;

/**
 * Created by zotal.102 on 01/06/17.
 */
public class AndroidImageAdapter extends PagerAdapter {

    Context mContext;

    AndroidImageAdapter(Context context) {

        this.mContext = context;
    }

    @Override
    public int getCount() {

        return sliderImagesId.length+1;

    }

    private int[] sliderImagesId = new int[]{

            R.mipmap.welcome,R.mipmap.slider_1,R.mipmap.slider_2,
            R.mipmap.slider_3,R.mipmap.slider_4
    };

    @Override
    public boolean isViewFromObject(View v, Object obj) {

        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {

         ImageView mImageView = new ImageView(mContext);
         mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

//        mImageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
//                sliderImagesId[i]));

        if(i < getCount()-1)

            try {

                 mImageView.setImageResource(sliderImagesId[i]);
        }

        catch (OutOfMemoryError e){

        }
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {

        ((ViewPager) container).removeView((ImageView) obj);
    }
}