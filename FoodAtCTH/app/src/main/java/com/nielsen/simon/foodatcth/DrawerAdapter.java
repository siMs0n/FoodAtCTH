package com.nielsen.simon.foodatcth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Simon on 2015-06-02.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>{

    private static final int TYPE_HEADER = 0; // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String appName;
    private String tagLine;
    private int appIcon;

    private ClickListener clickListener;

    public DrawerAdapter(String titles[], int icons[], String appName, String tagLine, int appIcon){
        mNavTitles = titles;
        mIcons = icons;
        this.appName = appName;
        this.tagLine = tagLine;
        this.appIcon = appIcon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener!=null){
                        clickListener.onClick(v, v.getId());
                    }
                }
            });

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);// Settingg the image with array of our icons
            holder.itemId = position-1;
        }
        else{

            holder.profile.setImageResource(appIcon);           // Similarly we set the resources for header view
            holder.Name.setText(appName);
            holder.email.setText(tagLine);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;
        int itemId;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            // Set the appropriate view in accordance with the the view type as passed when the holder object is created
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        public void onClick(View v, int itemID);
    }

}
