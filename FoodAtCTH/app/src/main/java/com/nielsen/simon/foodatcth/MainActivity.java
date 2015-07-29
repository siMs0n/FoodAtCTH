package com.nielsen.simon.foodatcth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nielsen.simon.foodatcth.adapters.DrawerAdapter;
import com.nielsen.simon.foodatcth.adapters.TabsAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //Variables for Navigation Drawer ------------------------------------
    private String titles[] = {"Campus Johanneberg", "Campus Lindholmen", "Sannegården", "Inställningar", "Hjälp och feedback"};
    private int icons[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    RecyclerView drawerRecyclerView, menuRecyclerView;
    DrawerAdapter drawerAdapter, menuAdapter;
    RecyclerView.LayoutManager drawerLayoutManager, menuLayoutManager;
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    String appName;
    String tagLine;
    int appIcon = R.mipmap.ic_launcher;
    //---------------------------------------------------------------------

    TabsAdapter tabsAdapter;
    ViewPager tabsPager;
    static final int NUM_ITEMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appName = this.getResources().getString(R.string.app_name);
        tagLine = this.getResources().getString(R.string.tag_line);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Create Navigation Drawer -------------------------------------------------

        drawerRecyclerView = (RecyclerView) findViewById(R.id.DrawerRecyclerView);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerAdapter(titles, icons, appName, tagLine, appIcon);
        drawerAdapter.setClickListener(new DrawerClickListener());

        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerLayoutManager = new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(drawerLayoutManager);

        drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I don't want anything happened when drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        // End Navigation Drawer -------------------------------------------------

        //Set up tabs ------------------------------------------------------------

        String[] tabTitles = {"Kårrestaurangen", "Linsen"};
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), NUM_ITEMS, tabTitles);

        tabsPager = (ViewPager) findViewById(R.id.viewpager);
        tabsPager.setAdapter(tabsAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(tabsPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.primaryColor));

        //End set up tabs --------------------------------------------------------
    }

    private class DrawerClickListener implements DrawerAdapter.ClickListener{
        @Override
        public void onClick(View v, int itemID) {
            selectItem(itemID);
        }
    }
    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int itemID) {

        if(itemID==R.id.item_row){

        }

        Intent a = new Intent(MainActivity.this, SannegardenGibraltar.class);
        startActivity(a);

        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /**getSupportFragmentManager().putFragment(savedInstanceState, "mContent", tabsAdapter.getItem(0));
         getSupportFragmentManager().putFragment(savedInstanceState, "mContent", tabsAdapter.getItem(1));
         getSupportFragmentManager().putFragment(savedInstanceState, "mContent", tabsAdapter.getItem(2));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
