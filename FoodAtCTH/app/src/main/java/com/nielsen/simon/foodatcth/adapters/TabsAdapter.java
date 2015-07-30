package com.nielsen.simon.foodatcth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nielsen.simon.foodatcth.fragments.RestaurantFragment;

/**
 * Created by Simon on 2015-06-06.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private String[] tabTitles;
    private boolean isJohanneberg;

    public TabsAdapter(FragmentManager fm, int numberOfTabs, String[] tabTitles, boolean isJohanneberg){
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.tabTitles = tabTitles;
        this.isJohanneberg = isJohanneberg;
    }

    @Override
    public Fragment getItem(int position) {
        return RestaurantFragment.newInstance(position, isJohanneberg);
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
