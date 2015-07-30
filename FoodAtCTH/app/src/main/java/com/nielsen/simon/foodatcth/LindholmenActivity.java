package com.nielsen.simon.foodatcth;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nielsen.simon.foodatcth.adapters.TabsAdapter;

/**
 * Created by Simon on 2015-07-30.
 */
public class LindholmenActivity extends AppCompatActivity {

    private Toolbar toolbar;

    TabsAdapter tabsAdapter;
    ViewPager tabsPager;
    static final int NUM_ITEMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lindholmen);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Set up tabs ------------------------------------------------------------

        String[] tabTitles = {"L's Kitchen", "Kokboken"};
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), NUM_ITEMS, tabTitles, true);

        tabsPager = (ViewPager) findViewById(R.id.viewpager);
        tabsPager.setAdapter(tabsAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(tabsPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        //End set up tabs --------------------------------------------------------

    }
}
