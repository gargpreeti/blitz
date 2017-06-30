package com.zoptal.blitz.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoptal.blitz.R;

public class Business_AdvHistoryDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_backarrow;
    private TextView txt_saleheading,txt_companycat,txt_saledesc,txt_storename,txt_storeaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_historydetail);

        initview();
    }


    public void initview(){

        img_backarrow=(ImageView)findViewById(R.id.img_backarrow);

        Intent intent = getIntent();
        String saleheading=intent.getStringExtra("saleheading");
        String  companycate=intent.getStringExtra("companycate");
        String  saledesc=intent.getStringExtra("saledesc");
        String  storename=intent.getStringExtra("storename");
        String storeaddess=intent.getStringExtra("storeaddess");

        txt_saleheading=(TextView)findViewById(R.id.txt_saleheading);
        txt_companycat=(TextView)findViewById(R.id.txt_companycat);
        txt_saledesc=(TextView)findViewById(R.id.txt_saledesc);
        txt_storename=(TextView)findViewById(R.id.txt_storename);
        txt_storeaddress=(TextView)findViewById(R.id.txt_storeaddress);


        txt_saleheading.setText(saleheading);
        txt_companycat.setText(companycate);
        txt_saledesc.setText(saledesc);
        txt_storename.setText(storename);
        txt_storeaddress.setText(storeaddess);

        img_backarrow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_backarrow:

                Intent i = new Intent(Business_AdvHistoryDetail.this, Business_AdvHistory.class);
                startActivity(i);
                finish();
                break;
//            case R.id.btn_information:
//
//                break;
//
//            case R.id.btn_payment:
//
//                break;

        }
    }
}