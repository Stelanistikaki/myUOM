package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class TweetListViewAdapter extends ArrayAdapter<Tweet> {
    private ArrayList<Tweet> dataSet;
    Context mContext;
    int mResource;

    public TweetListViewAdapter(Context context, int resource, ArrayList<Tweet> data) {
        super(context, R.layout.fragment_tweets, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tweetText = (TextView) convertView.findViewById(R.id.tweet_text);
        tweetText.setText(tweet.getText());

        tweetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/univofmacedonia/status/"+tweet.getId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
