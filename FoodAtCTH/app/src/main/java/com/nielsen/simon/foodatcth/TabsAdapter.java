package com.nielsen.simon.foodatcth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Simon on 2015-06-06.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public TabsAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return ArrayListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
