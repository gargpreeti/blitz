package com.zoptal.blitz.main;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zoptal.blitz.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout relative_viewdeals,relative_postdeals;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static String regId;
    public NotificationCompat.Builder notificationBuilder;
    public  String message = null,titlenotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
    }

    public void initview(){

        relative_viewdeals=(RelativeLayout)findViewById(R.id.relative_viewdeals);
        relative_postdeals=(RelativeLayout)findViewById(R.id.relative_postdeals);

        relative_viewdeals.setOnClickListener(this);
        relative_postdeals.setOnClickListener(this);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    message = intent.getStringExtra("message");
                    titlenotification = intent.getStringExtra("title");


                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle(titlenotification)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);


                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, notificationBuilder.build());
                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();


    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId= pref.getString("regId", null);

        Log.e("regid server news---",""+regId);
        if (!TextUtils.isEmpty(regId)) {
            //  Toast.makeText(getApplicationContext(), "Firebase Reg Id " + regId ,Toast.LENGTH_LONG).show();
            Log.e("Firebase Reg Id",""+regId);
        }
        else{
          //   Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!" ,Toast.LENGTH_LONG).show();
            Log.e("Firebase Reg Id",""+regId);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.relative_viewdeals:


                Intent intent= new Intent(MainActivity.this,MainActivity1.class);
                startActivity(intent);

                break;

            case R.id.relative_postdeals:

                Intent i = new Intent(MainActivity.this,Business_SigninActivity.class);
                startActivity(i);

                break;

        }

    }



}
