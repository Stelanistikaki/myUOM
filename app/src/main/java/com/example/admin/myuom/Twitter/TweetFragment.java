package com.example.admin.myuom.Twitter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.example.admin.myuom.R;
import com.example.admin.myuom.Twitter.Tweet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class TweetFragment extends Fragment {

    private ListView tweetsList;
    private ProgressBar tweetsProgressBar;

    public TweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        tweetsList = view.findViewById(R.id.tweets_list);
        tweetsProgressBar = view.findViewById(R.id.tweets_progress);

        run();

        return view;
    }

    public void run(){
        OkHttpClient client = new OkHttpClient();
        //get data from endpoint with hidden bearer token and cookies
        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/users/2874870377/tweets?tweet.fields=created_at&expansions=author_id&user.fields=created_at&max_results=100")
                .method("GET", null)
                .addHeader("Cookie", getString(R.string.cookies))
                .addHeader("Authorization", "Bearer "+getString(R.string.bearer_token))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("STELA1",e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //list with the tweets
                ArrayList<Tweet> data = null;

                try {
                    //fill the data list with Post objects
                    JSONObject obj = new JSONObject(responseBody.string());
                    JSONArray jsonArray = obj.getJSONArray("data");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Tweet>>(){}.getType();
                    data = gson.fromJson(jsonArray.toString(), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TweetListViewAdapter adapter = new TweetListViewAdapter(getContext(), R.layout.fragment_tweets_list, data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        tweetsProgressBar.setVisibility(View.GONE);
                        tweetsList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
