package com.example.connectly.seeker;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.connectly.seeker.SeekerEditFragment;
import com.example.connectly.seeker.SeekerInfoFragment;

public class SeekerPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;


    public SeekerPagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SeekerInfoFragment();
            case 1:
                return new SeekerEditFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
