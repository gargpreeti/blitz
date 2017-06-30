package com.zoptal.blitz.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zoptal.blitz.R;


public class AndroidImageSlider extends AppCompatActivity {

     ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_image_slider_activity);

        mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        final TextView txt_previous=(TextView)findViewById(R.id.txt_previous);
        final TextView txt_nxt=(TextView)findViewById(R.id.txt_nxt);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);

//        txt_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mViewPager.setCurrentItem(getItem(-1), true);
//
//            }
//        });


        txt_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
              //  mViewPager.setCurrentItem(getItem(+1), true);

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                if(position==0){
//                    txt_previous.setVisibility(View.GONE);
//
//                }
//                else{
//                    txt_previous.setVisibility(View.VISIBLE);
//
//                }
//                if(position==4){
//                    txt_nxt.setVisibility(View.GONE);
//                }
//                else{
//
//                    txt_nxt.setVisibility(View.VISIBLE);
//                }
                if(position==mViewPager.getAdapter().getCount()-1){
                    finish();

//                    Intent reg = new Intent(SlideActivity.this,activity_next.class);
//                    startActivity(reg);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() +i;
    }
}