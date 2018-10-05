package com.share.open_source.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mTitles;
    private List<Fragment> mFragmentList = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> newsFragmentList) {
        super(fm);
        mTitles = titles;
        mFragmentList = newsFragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}

