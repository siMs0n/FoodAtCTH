package com.nielsen.simon.foodatcth.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nielsen.simon.foodatcth.MenuAdapter;
import com.nielsen.simon.foodatcth.R;
import com.nielsen.simon.foodatcth.RssItem;
import com.nielsen.simon.foodatcth.RssReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampusJohannebergFragment extends Fragment {

    private int page;
    private int progressBarShowing=0;
    private boolean hasDisplayedErrorMsg;
    private ProgressBar progressBar;

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter menuAdapter;
    RecyclerView.LayoutManager menuLayoutManager;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Which pagenumber
     * @return A new instance of fragment CampusJohannebergFragment.
     */
    public static CampusJohannebergFragment newInstance(int page) {
        CampusJohannebergFragment fragment = new CampusJohannebergFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    public CampusJohannebergFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("page");
        }
        menuAdapter = new MenuAdapter();
        if (savedInstanceState != null) {
            String[] titles = savedInstanceState.getStringArray("titles");
            String[] descriptions = savedInstanceState.getStringArray("descriptions");
            if (titles != null && descriptions!=null) {
                ArrayList<RssItem> rssItems = new ArrayList<>();
                for(int i = 0; i<titles.length ;i++){
                    rssItems.add(new RssItem(titles[i],descriptions[i]));
                }
                ((MenuAdapter)menuAdapter).setRssItems(rssItems);
                Log.v("myApp", "Loaded tab ");
            }
        }
        Log.v("myApp", "Creating tab " + page);

    }

    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
       List<RssItem> rssItems = ((MenuAdapter)menuAdapter).getRssItems();
        String[] titles = new String[rssItems.size()];
        String[] descriptions = new String[rssItems.size()];
        for(int i = 0; i < rssItems.size(); i++){
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
        Log.v("myApp","Inflate fragment");

        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
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
        if(!((MenuAdapter)menuAdapter).hasBeenReset()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, 2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
            for (int i = 0; i < 5; i++) {
                String date = sdf.format(cal.getTime());
                String day = sdf2.format(cal.getTime());
                new RssTask(getActivity(), day).execute("http://cm.lskitchen.se/johanneberg/karrestaurangen/sv/" + date + ".rss");
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }
        }
        //String TODAY = sdf.format(new Date());


        // end read rss feed -----------------------------------------------------
        return v;
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

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> rssItems) {
            super.onPostExecute(rssItems);
            Log.v("myApp", "Executed");
            loadFoodMenu(rssItems, day);
            dismissLoadingAnimation();
        }
    }

    private void startLoadingAnimation(){
        if(progressBarShowing==0) {
            progressBar.setVisibility(View.VISIBLE);
            menuRecyclerView.setVisibility(View.GONE);
        }
        progressBarShowing++;
    }

    private void dismissLoadingAnimation(){
        progressBarShowing--;
        if(progressBarShowing==0) {
            menuRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadFoodMenu(List<RssItem> rssItems, String day){
        if(rssItems!=null) {
            Log.v("myApp", day + rssItems.size());
            ((MenuAdapter) menuAdapter).updateRssList(rssItems, day);
            menuAdapter.notifyDataSetChanged();
        }else if(rssItems==null && !hasDisplayedErrorMsg){
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.menu_error), Toast.LENGTH_SHORT).show();
            hasDisplayedErrorMsg = true;
        }
    }

}
