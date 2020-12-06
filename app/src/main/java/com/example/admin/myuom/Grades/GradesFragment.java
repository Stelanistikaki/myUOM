package com.example.admin.myuom.Grades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.R;
import com.example.admin.myuom.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class GradesFragment extends Fragment{

    private View view;
    private String id;
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
        semester = sp.getInt("semester",0);

        setupViewPagerGrades(viewPager);

        return view;

    }

    private void setupViewPagerGrades(ViewPager viewPager) {
        //create the tabs for the semesters
        ViewPagerAdapter gradesAdapter = new ViewPagerAdapter(getChildFragmentManager());
        gradesAdapter.addFragment(new ListGrades(id, 1, lessons), "1");
        gradesAdapter.addFragment(new ListGrades(id, 2, lessons), "2");
        gradesAdapter.addFragment(new ListGrades(id, 3, lessons), "3");
        gradesAdapter.addFragment(new ListGrades(id, 4, lessons), "4");
        gradesAdapter.addFragment(new ListGrades(id, 5, lessons), "5");
        gradesAdapter.addFragment(new ListGrades(id, 6, lessons), "6");
        gradesAdapter.addFragment(new ListGrades(id, 7, lessons), "7");
        gradesAdapter.addFragment(new ListGrades(id, 8, lessons), "8");
        viewPager.setAdapter(gradesAdapter);
        //-1 because it starts from 0
        viewPager.setCurrentItem(semester-1);
    }

}
