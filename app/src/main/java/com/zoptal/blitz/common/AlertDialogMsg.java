package com.zoptal.blitz.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoptal.blitz.R;


public class AlertDialogMsg {


    Context context;
    String msg;
    public   Dialog dialog;

    public AlertDialogMsg(Context context, String msg) {

        this.context = context;
        this.msg = msg;


        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alertdialogmsg);

        TextView tv_msg=(TextView)dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);


        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        TextView tv_ok=(TextView)dialog.findViewById(R.id.tv_ok);
        // TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);



        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
