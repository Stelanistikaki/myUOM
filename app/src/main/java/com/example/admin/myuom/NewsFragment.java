package com.example.admin.myuom;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private ListView newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsList = view.findViewById(R.id.news_list);


        BackgroundWorkerNews backgroundWorkerNews = new BackgroundWorkerNews(getContext());
        backgroundWorkerNews.execute();

        return view;
    }

    class BackgroundWorkerNews extends AsyncTask<String, String, String> {

        Context context;
        BackgroundWorkerNews(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String method = "GET";
            String url = "https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Fcreatefeed.fivefilters.org%2Fextract.php%3Furl%3Dhttps%253A%252F%252Fwww.uom.gr%252Fnea%26in_id_or_class%3Dpost-news-body%26url_contains%3D";
            String result="";

            try {
                URL Url = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)Url.openConnection();
                httpURLConnection.setRequestMethod(method);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject jsonObject = null;
            ArrayList<String> data = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = obj.getJSONArray("items");

                for(int i=0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    data.add(jsonObject.getString("title"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            NewsListViewAdapter adapter = new NewsListViewAdapter(getContext(), R.layout.fragment_news_list, data);
            newsList.setAdapter(adapter);


        }
    }
}
