package com.nielsen.simon.foodatcth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nielsen.simon.foodatcth.database.DbHandler;

import java.util.ArrayList;

/**
 * Created by Simon on 2015-07-20.
 */
public class SannegardenGibraltar extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Pizza> menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sannegarden_gibraltar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        DbHandler dbHandler = new DbHandler(this);
        menu = dbHandler.getPizzaMenu(DbHandler.PizzaMenu.SANNE_GIBRALTAR);
    }

}
