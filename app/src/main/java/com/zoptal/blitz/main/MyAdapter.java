package com.zoptal.blitz.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoptal.blitz.R;
import com.zoptal.blitz.common.AlertDialogMsg;
import com.zoptal.blitz.fragment.Fragment_BuyerAccountsetting;
import com.zoptal.blitz.fragment.Fragment_Favourites;
import com.zoptal.blitz.fragment.Fragment_LocMap;
import com.zoptal.blitz.fragment.Fragment_Notifications;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    Context context;
    // private SparseBooleanArray selectedPosition = new SparseBooleanArray();

    public int selectedPosition = 0;
    MainActivity1 activity1;
    public static int itemvalue=0;

    public  final String MyPREFERENCES = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    String buyer_access_token;
    public  static Fragment_BuyerAccountsetting fragment_buyerAccountsetting;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        LinearLayout root;


        public ViewHolder(final View itemView, int ViewType) {
            // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
            imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
            root = (LinearLayout) itemView.findViewById(R.id.root);
            // setting holder id as 1 as the object being populated are of type item row

        }
    }


    public MyAdapter(Context context, String Titles[], int Icons[]) { // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        this.context = context;
        activity1 = (MainActivity1) context;

        sharedpreferences1 =context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buyer_access_token = sharedpreferences1.getString("buyer_access_token", "");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // as the list view is going to be called after the header view so we decrement the
        // position by 1 and pass it to the holder while setting the text and image
        holder.textView.setText(mNavTitles[position]); // Setting the Text with the array of our Titles
        holder.imageView.setImageResource(mIcons[position]);// Settimg the image with array of our icons





        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "The Item Clicked is: " + "1", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        itemvalue=1;

                        Log.e("itemvalue===",""+itemvalue);

                        activity1.textToolHeader.setText("RESTAURANT");
                        Fragment fragment1=new Fragment_LocMap();
                        FragmentManager fragmentManager =((Activity)context).getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, fragment1).commit();
                        activity1.Drawer.closeDrawer(Gravity.LEFT);

                        break;
                    case 1:
                        itemvalue=2;

                        Log.e("itemvalue===",""+itemvalue);
                        activity1.textToolHeader.setText("BAR");
                        Fragment fragment2=new Fragment_LocMap();
                        FragmentManager fragmentManager1 =((Activity)context).getFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.activity_main_content_fragment, fragment2).commit();
                        activity1.Drawer.closeDrawer(Gravity.LEFT);

             break;

                    case 2:
                        itemvalue=3;
                        Log.e("itemvalue===",""+itemvalue);
                        activity1.textToolHeader.setText("RETAIL");
                        Fragment fragment3=new Fragment_LocMap();
                        FragmentManager fragmentManager2 =((Activity)context).getFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.activity_main_content_fragment, fragment3).commit();
                        activity1.Drawer.closeDrawer(Gravity.LEFT);
                        break;

                    case 3:
                        itemvalue=4;
                        activity1.textToolHeader.setText("THINGS TO DO");
                        Fragment fragment4=new Fragment_LocMap();
                        FragmentManager fragmentManager3 =((Activity)context).getFragmentManager();
                        fragmentManager3.beginTransaction().replace(R.id.activity_main_content_fragment, fragment4).commit();
                        activity1.Drawer.closeDrawer(Gravity.LEFT);


                        break;
                    case 4:
                        itemvalue=5;
                        activity1.textToolHeader.setText("TICKET & EVENT");
                        Fragment fragment5=new Fragment_LocMap();
                        FragmentManager fragmentManager4 =((Activity)context).getFragmentManager();
                        fragmentManager4.beginTransaction().replace(R.id.activity_main_content_fragment, fragment5).commit();
                        activity1.Drawer.closeDrawer(Gravity.LEFT);


                        break;
                    case 5:

                        if(buyer_access_token.isEmpty()){

                            AlertDialogMsg alertmsg = new
                                    AlertDialogMsg(context, "Please login to use this feature.");

                            alertmsg.dialog.show();
                        }
                        else {

                            activity1.textToolHeader.setText("Favourites");
                            Fragment fragment6 = new Fragment_Favourites();
                            FragmentManager fragmentManager6 = ((Activity) context).getFragmentManager();
                            fragmentManager6.beginTransaction().replace(R.id.activity_main_content_fragment, fragment6).commit();
                            activity1.Drawer.closeDrawer(Gravity.LEFT);
                        }

                        break;

                    case 6:

                        if(buyer_access_token.isEmpty()){

                            AlertDialogMsg alertmsg = new
                                    AlertDialogMsg(context, "Please Login to use this feature.");

                            alertmsg.dialog.show();
                        }
                        else {
                            activity1.textToolHeader.setText("Account Setting");
                            fragment_buyerAccountsetting = new Fragment_BuyerAccountsetting();
                            FragmentManager fragmentManager7 = ((Activity) context).getFragmentManager();
                            fragmentManager7.beginTransaction().replace(R.id.activity_main_content_fragment, fragment_buyerAccountsetting).commit();
                            activity1.Drawer.closeDrawer(Gravity.LEFT);

                        }
                  break;

                    case 7:

                        if(buyer_access_token.isEmpty()){

                            AlertDialogMsg alertmsg = new
                                    AlertDialogMsg(context, "Please Login to use this feature.");

                            alertmsg.dialog.show();
                        }
                        else {
                            activity1.textToolHeader.setText("Notifications");
                            Fragment  fragmentnoti = new Fragment_Notifications();
                            FragmentManager fragmentManager8 = ((Activity) context).getFragmentManager();
                            fragmentManager8.beginTransaction().replace(R.id.activity_main_content_fragment, fragmentnoti).commit();
                            activity1.Drawer.closeDrawer(Gravity.LEFT);

                        }
                        break;

                }
            }
        });

        Fragment fragment=new Fragment_LocMap();
        FragmentManager fragmentManager =((Activity)context).getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, fragment).commit();

    }

    @Override
    public int getItemCount() {

        return mNavTitles.length; // the number of items in the list will be +1 the titles including the header view.
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

}
