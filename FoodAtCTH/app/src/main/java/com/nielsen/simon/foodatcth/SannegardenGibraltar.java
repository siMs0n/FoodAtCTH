package com.nielsen.simon.foodatcth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.database.DbHandler;

import java.util.ArrayList;

/**
 * Created by Simon on 2015-07-20.
 */
public class SannegardenGibraltar extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Pizza> menu;

    RecyclerView menuRecyclerView;
    PizzaMenuAdapter menuAdapter;
    RecyclerView.LayoutManager menuLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sannegarden_gibraltar);

        toolbar = (Toolbar) findViewById(R.id.pizza_app_bar);
        setSupportActionBar(toolbar);

        //Set up basic menu ------------------------------------------------------

        menuRecyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);

        menuAdapter = new PizzaMenuAdapter();
        menuRecyclerView.setAdapter(menuAdapter);
        //menuRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        menuLayoutManager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(menuLayoutManager);
        // End set up basic menu -------------------------------------------------

        DbHandler dbHandler = new DbHandler(this);
        menu = dbHandler.getPizzaMenu(DbHandler.PizzaMenu.SANNE_GIBRALTAR);
        menuAdapter.setMenu(menu);
    }

}
