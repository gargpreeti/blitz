package com.zoptal.blitz.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zoptal.blitz.R;


public class AndroidImageSlider_Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_image_slider_activity);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        final TextView txt_nxt=(TextView)findViewById(R.id.txt_nxt);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);

        txt_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intentwillow= new Intent(AndroidImageSlider_Signup.this, Business_WillowClothingActivity.class);
                startActivity(intentwillow);
                //  mViewPager.setCurrentItem(getItem(+1), true);

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position==mViewPager.getAdapter().getCount()-1){
                    finish();
                    Intent intentwillow= new Intent(AndroidImageSlider_Signup.this, Business_WillowClothingActivity.class);
                    startActivity(intentwillow);
                    Log.e("reached last posi","ok");
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
}