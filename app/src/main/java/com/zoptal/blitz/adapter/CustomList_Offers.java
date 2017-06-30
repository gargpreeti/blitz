package com.zoptal.blitz.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
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
public class CustomList_Offers extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    private List<HomeFeeds_list> homefeed;
  //  String [] name;
    //String [] headingname;
   public  long milisecnds;

    public CustomList_Offers(Context context,List<HomeFeeds_list> homefeed) {

        this.context = context;
        this.homefeed=homefeed;
     //   this.headingname=headingname;
        inflater = LayoutInflater.from(context);

        // TODO Auto-generated constructor stub
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub

     //   Log.e("homefeedsize",""+homefeed.size());
//        if(homefeed.size()==0){
//            Fragment_LocMap.tv_msg.setVisibility(View.VISIBLE);
//            Fragment_LocMap.listview.setVisibility(View.GONE);
//        }
//        else{
//            Fragment_LocMap.tv_msg.setVisibility(View.GONE);
//            Fragment_LocMap.listview.setVisibility(View.VISIBLE);
//        }
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

        final Holdleroffer holder = new Holdleroffer();
        convertView = inflater.inflate(R.layout.customview_offers, null);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_headingname = (TextView) convertView.findViewById(R.id.tv_headingname);
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        holder.tv_remaining=(TextView) convertView.findViewById(R.id.tv_remaining);
        holder.tv_colr = (TextView) convertView.findViewById(R.id.tv_colr);
        holder.relative_list=(RelativeLayout)convertView.findViewById(R.id.relative_list);

        holder.tv_name.setText(homefeed.get(position).getBusiness_name());
        holder.tv_headingname.setText(homefeed.get(position).getSales_heading());
     //   holder.tv_colr.setBackgroundColor(Color.parseColor(homefeed.get(position).getColor_code()));

        String strttime= homefeed.get(position).getStarting_in_ss();

        if(strttime.equals("0")) {

            holder.tv_remaining.setText("Remaining");
            int remainingtym = Integer.parseInt(homefeed.get(position).getRemaing_time_ss());
            calculateTime(remainingtym);
        }
        else {
            holder.tv_remaining.setText("Starting in");
            int startingtym = Integer.parseInt(homefeed.get(position).getStarting_in_ss());
            calculateTime(startingtym);

        }

        new CountDownTimer(milisecnds, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes,elapsedSeconds);
                holder.tv_time.setText(yy);
            }

            public void onFinish() {

                holder.tv_time.setText("00:00:00");
            }
        }.start();




        holder.relative_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,Activity_ListDetail.class);
                context.startActivity(i);

            }
        });

       return convertView;

    }

    class Holdleroffer
    {
        TextView tv_name;
        TextView tv_headingname;
        TextView tv_time;
        TextView tv_colr;
        TextView tv_remaining;
        RelativeLayout relative_list;
    }

    public  void calculateTime(long seconds) {
//         int day = (int) TimeUnit.SECONDS.toDays(seconds);
//        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
//        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
//        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
//        long miliseconds = TimeUnit.SECONDS.toMillis(seconds) - (TimeUnit.SECONDS.toSeconds(seconds) *60);

        milisecnds=seconds*1000;
      //  Log.e(" tymmmmm","Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second +"Miliseconds "+  miliseconds);
        Log.e(" tymmmmm","Miliseconds "+  milisecnds);

    }
}



