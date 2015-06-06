package com.nielsen.simon.foodatcth.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nielsen.simon.foodatcth.MenuAdapter;
import com.nielsen.simon.foodatcth.R;
import com.nielsen.simon.foodatcth.RssItem;
import com.nielsen.simon.foodatcth.RssReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampusJohannebergFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampusJohannebergFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampusJohannebergFragment extends Fragment {

    private int page;

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter menuAdapter;
    RecyclerView.LayoutManager menuLayoutManager;

    private OnFragmentInteractionListener mListener;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_campus_johanneberg, container, false);

        //Set up basic menu ------------------------------------------------------

        menuRecyclerView = (RecyclerView) v.findViewById(R.id.MenuRecyclerView);
        menuRecyclerView.setHasFixedSize(true);
        menuAdapter = new MenuAdapter();

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
        for(int i = 0; i < 5; i++){
            String date = sdf.format(cal.getTime());
            String day = sdf2.format(cal.getTime());
            new RssTask(getActivity(), day).execute("http://cm.lskitchen.se/johanneberg/karrestaurangen/sv/"+date+".rss");
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        //String TODAY = sdf.format(new Date());


        // end read rss feed -----------------------------------------------------

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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

    private void startLoadingAnimation(){

    }

    private void dismissLoadingAnimation(){

    }

    private void loadFoodMenu(List<RssItem> rssItems, String day){
        ((MenuAdapter)menuAdapter).updateRssList(rssItems, day);
        menuAdapter.notifyDataSetChanged();
    }

}
