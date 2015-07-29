package com.nielsen.simon.foodatcth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nielsen.simon.foodatcth.fragments.CampusJohannebergFragment;

/**
 * Created by Simon on 2015-06-06.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private String[] tabTitles;

    public TabsAdapter(FragmentManager fm, int numberOfTabs, String[] tabTitles){
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return CampusJohannebergFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
