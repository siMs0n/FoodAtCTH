package com.nielsen.simon.foodatcth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.Pizza;
import com.nielsen.simon.foodatcth.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-07-26.
 */
public class PizzaMenuAdapter extends RecyclerView.Adapter<PizzaMenuAdapter.ViewHolder> {

    private List<Pizza> menu;

    private static final int LIST_ITEM = 0;
    private static final int LIST_TITLE = 1;
    private static final int LIST_HEADER = 2;

    public PizzaMenuAdapter() {
        menu = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nr, name, ingredients, price;
        public int view_type;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            view_type = viewType;
            if(viewType == LIST_ITEM) {
                nr = (TextView)itemView.findViewById(R.id.pizzaNr);
                name = (TextView)itemView.findViewById(R.id.pizzaName);
                ingredients = (TextView)itemView.findViewById(R.id.pizzaIngredients);
                price = (TextView)itemView.findViewById(R.id.pizzaPrice);
            }
        }
    }

    public void setMenu(List<Pizza> menu){
        this.menu = menu;
        notifyDataSetChanged();
    }

    public List<Pizza> getMenu(){
        return menu;
    }


    @Override
    public PizzaMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pizza, parent, false); //Inflating the layout

        return new ViewHolder(v, viewType);
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nr.setText(menu.get(position).getNr()+".");
        holder.name.setText(menu.get(position).getName());
        holder.ingredients.setText(menu.get(position).getIngredients());
        holder.price.setText(menu.get(position).getPrice() + " kr");
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return LIST_ITEM;
    }

}

