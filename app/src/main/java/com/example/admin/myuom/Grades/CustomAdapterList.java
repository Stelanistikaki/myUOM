package com.example.admin.myuom.Grades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.myuom.R;

import java.util.ArrayList;

public class CustomAdapterList extends ArrayAdapter<Grade> {
    private ArrayList<Grade> dataSet;
    Context mContext;
    int mResource;

    public CustomAdapterList(Context context, int resource, ArrayList<Grade> data) {
        super(context, R.layout.fragment_grades, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Grade grade = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lessonText = (TextView) convertView.findViewById(R.id.lessonText);
        TextView gradeText = (TextView) convertView.findViewById(R.id.gradeText);

        lessonText.setText(grade.getName());
        gradeText.setText(grade.getGrade());

        return convertView;
    }
}
