package com.nielsen.simon.foodatcth.fragments;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nielsen.simon.foodatcth.database.DbHandlerRSS;
import com.nielsen.simon.foodatcth.dialog.MenuDetailDialog;
import com.nielsen.simon.foodatcth.adapters.MenuAdapter;
import com.nielsen.simon.foodatcth.R;
import com.nielsen.simon.foodatcth.RssItem;
import com.nielsen.simon.foodatcth.RssReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RestaurantFragment extends Fragment {

    private int page;
    private int progressBarShowing = 0;
    private boolean isOnlyExpress, isJohanneberg;
    private boolean hasDisplayedErrorMsg;
    private ProgressBar progressBar;
    private DbHandlerRSS dbHandlerRSS;
    private List<RssItem> menuItems;
    private List<RssItem> downloadedMenuItems;
    private DbHandlerRSS.Restaurant restaurant;

    private static int[] URL_IDS_JOHANNEBERG = new int[]{R.string.student_union_restaurant_link, R.string.linsen_link};
    private static DbHandlerRSS.Restaurant[] restaurantsJohanneberg = {DbHandlerRSS.Restaurant.STUDENT_UNION_RESTAURANT, DbHandlerRSS.Restaurant.LINSEN};
    private static int[] URL_IDS_LINDHOLMEN = new int[]{R.string.l_kitchen_link, R.string.student_union_restaurant_link};
    private static DbHandlerRSS.Restaurant[] restaurantsLindholmen = {DbHandlerRSS.Restaurant.LSKITCHEN, DbHandlerRSS.Restaurant.STUDENT_UNION_RESTAURANT};

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter menuAdapter;
    RecyclerView.LayoutManager menuLayoutManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Which pagenumber
     * @return A new instance of fragment RestaurantFragment.
     */
    public static RestaurantFragment newInstance(int page, boolean isJohanneberg) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putBoolean("isJohanneberg", isJohanneberg);
        fragment.setArguments(args);
        return fragment;
    }

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("page");
            isJohanneberg = getArguments().getBoolean("isJohanneberg");
            if(isJohanneberg && page==1){
                isOnlyExpress = true;
            }
        }
        menuAdapter = new MenuAdapter();
        if (savedInstanceState != null) {
            String[] titles = savedInstanceState.getStringArray("titles");
            String[] descriptions = savedInstanceState.getStringArray("descriptions");
            if (titles != null && descriptions != null) {
                ArrayList<RssItem> rssItems = new ArrayList<>();
                for (int i = 0; i < titles.length; i++) {
                    rssItems.add(new RssItem(titles[i], descriptions[i]));
                }
                ((MenuAdapter) menuAdapter).setRssItems(rssItems);
                Log.v("myApp", "Loaded tab ");
            }
        }
        Log.v("myApp", "Creating tab " + page);

        dbHandlerRSS = DbHandlerRSS.getInstance(getActivity());
        if(isJohanneberg){
            restaurant = restaurantsJohanneberg[page];
        }else{
            restaurant = restaurantsLindholmen[page];
        }
        ArrayList<RssItem> dbMenu = dbHandlerRSS.getMenuForRestaurant(restaurant);
        if (!dbMenu.isEmpty()) {
            loadFoodMenu(dbMenu, true);
        }
        downloadedMenuItems = new ArrayList<>();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("myApp", "Stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("myApp", "Destroy View");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("myApp", "Destroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("myApp", "Pause");
    }

    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        List<RssItem> rssItems = ((MenuAdapter) menuAdapter).getRssItems();
        String[] titles = new String[rssItems.size()];
        String[] descriptions = new String[rssItems.size()];
        for (int i = 0; i < rssItems.size(); i++) {
            titles[i] = rssItems.get(i).getTitle();
            descriptions[i] = rssItems.get(i).getDescription();
        }
        savedState.putStringArray("titles", titles);
        savedState.putStringArray("descriptions", descriptions);
        Log.v("myApp", "Saved tab ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_campus_johanneberg, container, false);
        Log.v("myApp", "Inflate fragment");

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //Set up basic menu ------------------------------------------------------

        menuRecyclerView = (RecyclerView) v.findViewById(R.id.MenuRecyclerView);
        menuRecyclerView.setHasFixedSize(true);


        menuRecyclerView.setAdapter(menuAdapter);
        menuLayoutManager = new LinearLayoutManager(container.getContext());
        //New version
        //menuRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        menuRecyclerView.setLayoutManager(menuLayoutManager);
        // End set up basic menu -------------------------------------------------
        // Read rss feed ---------------------------------------------------------
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
        String[] urls = new String[5];
        String[] days = new String[5];
        for (int i = 0; i < 5; i++) {
            String date = sdf.format(cal.getTime());
            String day = sdf2.format(cal.getTime());
            if(isJohanneberg)
                urls[i] = getResources().getString(URL_IDS_JOHANNEBERG[page]) + date + ".rss";
            else
                urls[i] = getResources().getString(URL_IDS_LINDHOLMEN[page]) + date + ".rss";
            String capDay = day.substring(0, 1).toUpperCase() + day.substring(1);
            days[i] = capDay;
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        ((MenuAdapter) menuAdapter).setTitleNames(days);
        new RssTask(days).execute(urls);

        ((MenuAdapter) menuAdapter).setClickListener(new MenuAdapter.ClickListener() {
            @Override
            public void onClick(View v, Drawable image, String title, String description) {
                MenuDetailDialog.newInstance(image, title, description).show(getFragmentManager().beginTransaction(), "detailsmenufragment");
            }
        });


        // end read rss feed -----------------------------------------------------
        return v;
    }

    private class RssTask extends AsyncTask<String, Void, List<RssItem>> {
        private String[] days;

        public RssTask(String[] days) {
            this.days = days;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadingAnimation();
        }

        @Override
        protected List<RssItem> doInBackground(String[] urls) {

            try {
                RssReader rssReader = new RssReader(urls, days);
                Log.v("myApp", "Reading rss from doInBackground");
                return rssReader.readRss();
            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> rssItems) {
            super.onPostExecute(rssItems);
            Log.v("myApp", "Executed");
            if(isOnlyExpress){
                rssItems = isolateExpressItems(rssItems, days);
            }
            loadFoodMenu(rssItems, false);
            dismissLoadingAnimation();
        }
    }

    private void startLoadingAnimation() {
        if (progressBarShowing == 0) {
            progressBar.setVisibility(View.VISIBLE);
            menuRecyclerView.setVisibility(View.GONE);
        }
        progressBarShowing++;
    }

    private void dismissLoadingAnimation() {
        progressBarShowing--;
        if (progressBarShowing == 0) {
            menuRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadFoodMenu(List<RssItem> rssItems, boolean fromDB) {
        if (rssItems != null && isUpdatedMenu(rssItems)) {
            ((MenuAdapter) menuAdapter).setRssItems(rssItems);
            menuAdapter.notifyDataSetChanged();
            menuItems = rssItems;
            dismissLoadingAnimation();
            if (!fromDB) {
                dbHandlerRSS.replaceRestaurantMenu(restaurant, rssItems);
            }
        } else if (rssItems == null && !hasDisplayedErrorMsg) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.menu_error), Toast.LENGTH_SHORT).show();
            hasDisplayedErrorMsg = true;
        }
    }

    private boolean isUpdatedMenu(List<RssItem> newRssItems) {
        if (menuItems == null) {
            return true;
        } else if (newRssItems.size() != menuItems.size()) {
            return true;
        }

        for (int i = 0; i < newRssItems.size(); i++) {
            if (!newRssItems.get(i).getTitle().equalsIgnoreCase(menuItems.get(i).getTitle()) ||
                    !newRssItems.get(i).getDescription().equalsIgnoreCase(menuItems.get(i).getDescription())) {
                return true;
            }
        }

        return false;
    }

    private List<RssItem> isolateExpressItems(List<RssItem> rssItems, String[] days){
        ArrayList<RssItem> newRssItemList = new ArrayList<>();

        for(int i = 0; i<rssItems.size(); i++){
            for(String day: days){
                if(rssItems.get(i).getTitle().equalsIgnoreCase(day)){
                    newRssItemList.add(rssItems.get(i));
                    break;
                }
            }
            if(rssItems.get(i).getTitle().equalsIgnoreCase("Xpress")){
                newRssItemList.add(rssItems.get(i));
            }
        }
        return  newRssItemList;
    }

}
