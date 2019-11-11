package com.example.admin.myuom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> dataSet;
    Context mContext;
    int mResource;

    public NewsListViewAdapter(Context context, int resource, ArrayList<String> data) {
        super(context, R.layout.fragment_news, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String newsTitle = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView newsTitleText = (TextView) convertView.findViewById(R.id.newsTitleText);

        newsTitleText.setText(newsTitle);

        return convertView;
    }
}
