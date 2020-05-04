package com.example.admin.myuom.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnpassedLessonsListAdapter extends ArrayAdapter<Lesson> {
    private ArrayList<Lesson> dataSet;
    Context mContext;
    int mResource;

    //constructor
    public UnpassedLessonsListAdapter(Context context, int resource, ArrayList<Lesson> data) {
        super(context, R.layout.fragment_settings, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //set up the listview
        Lesson lesson = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lessonText = (TextView) convertView.findViewById(R.id.unpasslessonText);
        TextView semesterText = (TextView) convertView.findViewById(R.id.unpassSemesterText);

        lessonText.setText(lesson.getName());
        semesterText.setText(String.valueOf(lesson.getSemester()));

        return convertView;
    }

}
