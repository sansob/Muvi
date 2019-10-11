package com.qakap.muvi.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qakap.muvi.fragment.Genres;
import com.qakap.muvi.fragment.Home;


public class TabAdapter extends FragmentPagerAdapter {
    int NumberOfTabs;

    public TabAdapter(FragmentManager fm, int mnumoftabs) {
        super(fm);
        this.NumberOfTabs = mnumoftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Home tab1 = new Home();
                return tab1;

            case 1:
                Genres tab2 = new Genres();
                return tab2;

            case 2:
                Home tab3 = new Home();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NumberOfTabs;
    }
}
