package com.example.admin.myuom.News;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.admin.myuom.R;
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

public class NewsFragment extends Fragment {

    private ListView newsList;
    private String link="";
    private ProgressBar newsProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsList = view.findViewById(R.id.news_list);
        newsProgressBar = view.findViewById(R.id.news_progress);

        run();

        //if the user clicks in an item of the list a new fragment with detailed view is created
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send the link of the item to detailed view
                Post aPost =(Post) parent.getItemAtPosition(position);
                link = aPost.getLink();
                Fragment fragment = new DetailNewsFragment(link);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void run(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //rss feed that gives the last 5 (4 visible) news of the news page in uom.gr
                .url("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Fcreatefeed.fivefilters.org%2Fextract.php%3Furl%3Dhttps%253A%252F%252Fwww.uom.gr%252Fnea%26in_id_or_class%3Dpost-news-body%26url_contains%3D")
                .build();

        newsProgressBar.setVisibility(View.VISIBLE);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                ArrayList<Post> data = null;

                try {
                    //fill the data list with Post objects
                    JSONObject obj = new JSONObject(responseBody.string());
                    JSONArray jsonArray = obj.getJSONArray("items");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                    data = gson.fromJson(jsonArray.toString(), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //the first item has no title so its out
                //rss bug
                for(int i=0; i<data.size();i++){
                    if(data.get(i).getTitle().equals("")){
                        data.remove(i);
                    }
                }

                NewsListViewAdapter adapter = new NewsListViewAdapter(getContext(), R.layout.fragment_news_list, data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        newsProgressBar.setVisibility(View.GONE);
                        newsList.setAdapter(adapter);
                    }
                });
            }
        });
    }

}
