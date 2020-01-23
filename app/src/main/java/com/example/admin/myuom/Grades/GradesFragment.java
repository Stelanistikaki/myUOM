package com.example.admin.myuom.Grades;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.admin.myuom.R;
import com.google.android.material.tabs.TabLayout;


public class GradesFragment extends Fragment{

    View view;
    String id, direction;
    int semester;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_grades, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // this = fragment
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        //get the shared values
        id = sp.getString("id", "");
        direction = sp.getString("direction", "");
        semester = sp.getInt("semester",0);
        setupViewPager(viewPager);

        //check if there is internet connection
        if(!isOnline()){
            view = inflater.inflate(R.layout.no_internet, container, false);

        }
        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        //create the tabs for the semesters
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new ListGrades(id, 1, direction), "1");
        adapter.addFragment(new ListGrades(id, 2, direction), "2");
        adapter.addFragment(new ListGrades(id, 3, direction), "3");
        adapter.addFragment(new ListGrades(id, 4, direction), "4");
        adapter.addFragment(new ListGrades(id, 5, direction), "5");
        adapter.addFragment(new ListGrades(id, 6, direction), "6");
        adapter.addFragment(new ListGrades(id, 7, direction), "7");
        adapter.addFragment(new ListGrades(id, 8, direction), "8");
        viewPager.setAdapter(adapter);
        //-1 because it starts from 0
        viewPager.setCurrentItem(semester-1);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
