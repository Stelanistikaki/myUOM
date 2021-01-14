package com.example.admin.myuom;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //set up the page adapter for the tabs

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    //get each item for the tab
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    //get count of the tabs
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    //replace the fragment
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }
}
