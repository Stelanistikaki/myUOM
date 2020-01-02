package com.example.admin.myuom.News;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.myuom.R;

import java.util.ArrayList;

public class NewsListViewAdapter extends ArrayAdapter<Post> {
    private ArrayList<Post> dataSet;
    Context mContext;
    int mResource;

    public NewsListViewAdapter(Context context, int resource, ArrayList<Post> data) {
        super(context, R.layout.fragment_news, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        Button addEvent = convertView.findViewById(R.id.add_event);
        TextView newsTitleText = (TextView) convertView.findViewById(R.id.newsTitleText);

        if(!post.getTitle().equals(""))
            newsTitleText.setText(post.getTitle());

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, newsTitleText.getText());
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Πανεπιστήμιο Μακεδονίας");
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                getContext().startActivity(calIntent);
            }
        });

        return convertView;
    }
}
