package com.nielsen.simon.foodatcth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.R;
import com.nielsen.simon.foodatcth.RssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-06-04.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<RssItem> rssItems;
    private ArrayList<String> titleNames;

    private boolean reseted;

    private static final int LIST_ITEM = 0;
    private static final int LIST_TITLE = 1;
    private static final int LIST_HEADER = 2;

    public MenuAdapter() {
        rssItems = new ArrayList<>();
        titleNames = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView image;
        public int view_type;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            view_type = viewType;
            if(viewType == LIST_ITEM) {
                title = (TextView) itemView.findViewById(R.id.menuItemTitle);
                description = (TextView) itemView.findViewById(R.id.menuItemDescription);
                image = (ImageView) itemView.findViewById(R.id.menuItemImage);
            }else if(viewType == LIST_TITLE || viewType == LIST_HEADER){
                title = (TextView) itemView.findViewById(R.id.menuTitle);
            }
        }
    }

    public void updateRssList(List<RssItem> rssItems) {
        this.rssItems.addAll(rssItems);
        reseted = true;
    }

    public void setRssItems(List<RssItem> items){
        rssItems = items;
        reseted = true;
    }

    public List<RssItem> getRssItems(){
        return rssItems;
    }

    public boolean hasBeenReset(){
        return reseted;
    }

    public void setTitleNames(String[] titles){
        for(int i = 0; i<titles.length; i++){
            titleNames.add(titles[i]);
        }
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == LIST_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false); //Inflating the layout
        }else if(viewType == LIST_HEADER){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false); //Inflating the layout
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_title, parent, false); //Inflating the layout
        }

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object

        //inflate your layout and pass it to view holder
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position>=rssItems.size()){
            holder.title.setText(position);
        }
        if(holder.view_type == LIST_ITEM) {
            holder.title.setText(rssItems.get(position).getTitle());
            if (rssItems.get(position).getDescription().isEmpty()) {
                holder.description.setText("Ingen " + rssItems.get(position).getTitle() + " idag.");
            } else {
                holder.description.setText(rssItems.get(position).getDescription());
            }
            switch (rssItems.get(position).getTitle()) {
                case "Classic Kött":
                    holder.image.setImageResource(R.drawable.meat);
                    break;
                case "Classic Fisk":
                    holder.image.setImageResource(R.drawable.fish);
                    break;
                case "Veckans Soppa":
                    holder.image.setImageResource(R.drawable.soup);
                    break;
                case "Xpress":
                    holder.image.setImageResource(R.drawable.xpress);
                    break;
                case "Gröna väggen":
                    holder.image.setImageResource(R.drawable.veg);
                    break;
            }
        }else{
            holder.title.setText(rssItems.get(position).getTitle());
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return LIST_HEADER;

        if (titleNames.contains(rssItems.get(position).getTitle()))
            return LIST_TITLE;

        return LIST_ITEM;
    }

}
