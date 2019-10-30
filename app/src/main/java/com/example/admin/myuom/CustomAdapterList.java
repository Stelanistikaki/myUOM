package com.example.admin.myuom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterList extends ArrayAdapter<Lesson> {
    private ArrayList<Lesson> dataSet;
    Context mContext;
    int mResource;

    public CustomAdapterList(Context context, int resource, ArrayList<Lesson> data) {
        super(context, R.layout.activity_grades, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Lesson lesson = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lessonText = (TextView) convertView.findViewById(R.id.lessonText);
        TextView gradeText = (TextView) convertView.findViewById(R.id.gradeText);

        lessonText.setText(lesson.getName());
        gradeText.setText(lesson.getGrade());

        return convertView;
    }
}
