package com.example.admin.myuom.Grades;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myuom.Program.Lesson;
import com.example.admin.myuom.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GradesFragment extends Fragment{

    private View view;
    private String id, direction;
    private int semester;
    private ArrayList<Lesson> lessons;


    public GradesFragment(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

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

        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        //create the tabs for the semesters
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ListGrades(id, 1, lessons), "1");
        adapter.addFragment(new ListGrades(id, 2, lessons), "2");
        adapter.addFragment(new ListGrades(id, 3, lessons), "3");
        adapter.addFragment(new ListGrades(id, 4, lessons), "4");
        adapter.addFragment(new ListGrades(id, 5, lessons), "5");
        adapter.addFragment(new ListGrades(id, 6, lessons), "6");
        adapter.addFragment(new ListGrades(id, 7, lessons), "7");
        adapter.addFragment(new ListGrades(id, 8, lessons), "8");
        viewPager.setAdapter(adapter);
        //-1 because it starts from 0
        viewPager.setCurrentItem(semester-1);
    }

}
