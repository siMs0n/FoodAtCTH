package com.nielsen.simon.foodatcth;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Simon on 2015-06-04.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<RssItem> rssItems;

    public MenuAdapter(List<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;

        public ViewHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rowText);
            description = (TextView) itemView.findViewById(R.id.rowText);
        }
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object

        //inflate your layout and pass it to view holder
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(rssItems.get(position).getTitle());
            holder.description.setText(rssItems.get(position).getDescription());
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
