package com.example.admin.myuom.Program;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.admin.myuom.ViewPagerAdapter;
import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Calendar;


public class ProgramFragment extends Fragment {

    String id;
    int semester;
    ArrayList<Lesson> lessons;

    public ProgramFragment (ArrayList<Lesson> lessons){
        this.lessons = lessons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        ViewPager programViewPager = (ViewPager) view.findViewById(R.id.prog_viewpager);
        TabLayout programTabLayout = (TabLayout) view.findViewById(R.id.prog_tabs);
        programTabLayout.setupWithViewPager(programViewPager);

        // this = fragment
        //get the shared values to show the correct data
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        id = sp.getString("id", "");
        semester = sp.getInt("semester", 0);

        setupViewPagerProgram(programViewPager);

        return view;
    }

    private void setupViewPagerProgram(ViewPager viewPager) {
        //create the tabs for the semesters
        ViewPagerAdapter programAdapter = new ViewPagerAdapter(getChildFragmentManager());
        programAdapter.addFragment(new ListProgram(id, semester, lessons, 1), "Δ");
        programAdapter.addFragment(new ListProgram(id, semester, lessons, 2), "Τρ");
        programAdapter.addFragment(new ListProgram(id, semester, lessons, 3), "Τε");
        programAdapter.addFragment(new ListProgram(id, semester, lessons, 4), "Πε");
        programAdapter.addFragment(new ListProgram(id, semester, lessons, 5), "Πα");
        viewPager.setAdapter(programAdapter);
        //-1 because it starts from 0
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int selection;
        if(dayOfWeek == 1 || dayOfWeek == 7)
            selection = 0;
        else
            //because 1 is Sunday and it starts from 0
            selection = dayOfWeek-2;
        viewPager.setCurrentItem(selection);
    }



}
