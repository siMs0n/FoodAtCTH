package com.nielsen.simon.foodatcth;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.adapters.PizzaMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-08-04.
 */
public class CardSanneGroup extends CardView{

    private int price, groupNr;
    private String title;
    private boolean showPrice, priceSet;
    List<Pizza> pizzaList;

    private RecyclerView menuRecyclerView;
    private PizzaMenuAdapter menuAdapter;
    private RecyclerView.LayoutManager menuLayoutManager;

    public CardSanneGroup(Context context, List<Pizza> pizzaList, int groupNr){
        super(context);
        init(pizzaList, groupNr);
    }

    public CardSanneGroup(Context context, AttributeSet attrs, List<Pizza> pizzaList, int groupNr) {
        super(context, attrs);
        init(pizzaList, groupNr);
    }

    public CardSanneGroup(Context context, AttributeSet attrs, int defStyleAttr, List<Pizza> pizzaList, int groupNr) {
        super(context, attrs, defStyleAttr);
        init(pizzaList, groupNr);
    }

    private void init(List<Pizza> pizzaList, int groupNr) {
        inflate(getContext(), R.layout.card_sanne_group, this);

        this.groupNr = groupNr;
        ArrayList<Pizza> groupPizzaList = new ArrayList<>();
        if(!pizzaList.isEmpty()){
            price = pizzaList.get(0).getPrice();
        }
        for(Pizza pizza: pizzaList){
            if(pizza.getGroupNr() == groupNr){
                groupPizzaList.add(pizza);
                Log.v("myApp", "Adding pizza " + pizza.getName());
                if(!priceSet){
                    price = pizza.getPrice();
                    priceSet = true;
                    showPrice = true;
                }
                if(pizza.getPrice()!=price){
                    showPrice = false;
                }
            }
        }
        this.pizzaList = groupPizzaList;
        Log.v("myApp", "List size: " + this.pizzaList.size());
        if(showPrice){
            ((TextView)findViewById(R.id.cardPriceText)).setText(price + " kr");
        }else{
            findViewById(R.id.cardPriceText).setVisibility(GONE);
        }
        ((TextView)findViewById(R.id.cardTitleText)).setText(getContext().getResources().getString(R.string.pizzagroup) + " " + groupNr);

        menuRecyclerView = (RecyclerView) findViewById(R.id.cardMenuRecyclerView);

        menuAdapter = new PizzaMenuAdapter();
        menuRecyclerView.setAdapter(menuAdapter);
        menuLayoutManager = new LinearLayoutManager(getContext());
        menuRecyclerView.setLayoutManager(menuLayoutManager);

        menuAdapter.setMenu(this.pizzaList);
    }

}
