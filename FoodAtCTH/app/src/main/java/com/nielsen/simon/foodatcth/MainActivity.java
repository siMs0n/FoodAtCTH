package com.nielsen.simon.foodatcth;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //Variables for Navigation Drawer ------------------------------------
    private String titles[] = {"Campus Johanneberg","Campus Lindholmen","Sannegården","Inställningar","Hjälp och feedback"};
    private int icons[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    RecyclerView drawerRecyclerView, menuRecyclerView;
    RecyclerView.Adapter drawerAdapter, menuAdapter;
    RecyclerView.LayoutManager drawerLayoutManager, menuLayoutManager;
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    String appName;
    String tagLine;
    int appIcon = R.mipmap.ic_launcher;
    //---------------------------------------------------------------------

    TabsAdapter tabsAdapter;
    ViewPager tabsPager;
    static final int NUM_ITEMS = 10;

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

        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerLayoutManager = new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(drawerLayoutManager);

        drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close) {

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

        //Set up basic menu ------------------------------------------------------

        menuRecyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);
        menuRecyclerView.setHasFixedSize(true);
        menuAdapter = new MenuAdapter();

        menuRecyclerView.setAdapter(menuAdapter);
        menuLayoutManager = new LinearLayoutManager(this);
        //New version
        //menuRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        menuRecyclerView.setLayoutManager(menuLayoutManager);

        // End set up basic menu -------------------------------------------------

        //Set up tabs ------------------------------------------------------------

        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), NUM_ITEMS);

        tabsPager = (ViewPager)findViewById(R.id.pager);
        tabsPager.setAdapter(tabsAdapter);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.goto_first);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabsPager.setCurrentItem(0);
            }
        });
        button = (Button)findViewById(R.id.goto_last);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabsPager.setCurrentItem(NUM_ITEMS-1);
            }
        });

        //End set up tabs --------------------------------------------------------

        // Read rss feed ---------------------------------------------------------
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
        for(int i = 0; i < 5; i++){
            String date = sdf.format(cal.getTime());
            String day = sdf2.format(cal.getTime());
            new RssTask(this, day).execute("http://cm.lskitchen.se/johanneberg/karrestaurangen/sv/"+date+".rss");
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        //String TODAY = sdf.format(new Date());


        // end read rss feed -----------------------------------------------------
    }

    private class RssTask extends AsyncTask<String, Void, List<RssItem>> {
        private Activity activity;
        private String day;
        public RssTask(Activity a, String day){
            activity = a;
            this.day = day;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadingAnimation();
        }

        @Override
        protected List<RssItem> doInBackground(String... urls) {

            try{
                RssReader rssReader = new RssReader(urls[0]);
                return rssReader.readRss();
            }catch (IOException e){
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, activity.getResources().getString(R.string.menu_error), Toast.LENGTH_SHORT);
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> rssItems) {
            super.onPostExecute(rssItems);
            loadFoodMenu(rssItems, day);
            dismissLoadingAnimation();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startLoadingAnimation(){

    }

    private void dismissLoadingAnimation(){

    }

    private void loadFoodMenu(List<RssItem> rssItems, String day){
        ((MenuAdapter)menuAdapter).updateRssList(rssItems, day);
        menuAdapter.notifyDataSetChanged();
    }
}
