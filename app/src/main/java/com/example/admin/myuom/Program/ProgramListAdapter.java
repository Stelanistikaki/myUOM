package com.example.admin.myuom.Program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.myuom.R;

import java.util.ArrayList;

public class ProgramListAdapter extends ArrayAdapter<Program> {

    private ArrayList<Program> dataSet;
    Context mContext;
    int mResource;

    public ProgramListAdapter(Context context, int resource, ArrayList<Program> data) {
        super(context, R.layout.fragment_program_list, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Program theProgram = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView timeText = (TextView) convertView.findViewById(R.id.timeText);
        TextView lessonTitleText = (TextView) convertView.findViewById(R.id.lessonText);
        TextView classText = convertView.findViewById(R.id.classText);

        //set the textviews and the text for button
        timeText.setText(theProgram.getTime());
        lessonTitleText.setText(theProgram.getTitle());
        classText.setText(theProgram.getClassroom());

        return convertView;
    }
}
