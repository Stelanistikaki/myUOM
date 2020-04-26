package com.example.admin.myuom.Grades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.myuom.R;

import java.util.ArrayList;

public class GradesListAdapter extends ArrayAdapter<Grade> {
    private ArrayList<Grade> dataSet;
    Context mContext;
    int mResource;

    //constructor
    public GradesListAdapter(Context context, int resource, ArrayList<Grade> data) {
        super(context, R.layout.fragment_grades, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //set up the listview
        Grade grade = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lessonText = (TextView) convertView.findViewById(R.id.lessonText);
        TextView gradeText = (TextView) convertView.findViewById(R.id.gradeText);
        ImageView gradeIcon = convertView.findViewById(R.id.gradeIcon);

        lessonText.setText(grade.getName());
        gradeText.setText(grade.getGrade());
        //this goes first because "-" does not an integer value so it crashes
        if(grade.getGrade().equals(("-"))) {
            gradeIcon.setImageResource(R.drawable.not_graded);
        }
        //if its above 5 (or 5) the lesson is passed
        else if(Integer.valueOf(grade.getGrade()) >= 5){
            gradeIcon.setImageResource(R.drawable.passed_lesson);
        }
        //if its not above 5 and its not - then its failed
        else{
            gradeIcon.setImageResource(R.drawable.failed_lesson);
        }

        return convertView;
    }
}
