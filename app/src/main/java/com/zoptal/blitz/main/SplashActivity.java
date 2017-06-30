package com.zoptal.blitz.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zoptal.blitz.R;


//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

      // Fabric.with(this, new Crashlytics());
       setContentView(R.layout.activity_splash);


       Thread t1=new Thread()
       {
           public void run()
           {
               try{

                Thread.sleep(2000);

                   Intent i = new Intent(SplashActivity.this,MainActivity.class);
                   startActivity(i);
                   finish();

               }
               catch(Exception e)
               {

               }
           }
       };
       t1.start();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.

     //  getMenuInflater().inflate(R., menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

       //noinspection SimplifiableIfStatement
//       if (id == R.id.action_settings) {
//           return true;
//       }

       return super.onOptionsItemSelected(item);
   }

}
