package com.example.admin.myuom.News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.admin.myuom.R;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DetailNewsFragment extends Fragment {
    private String link;
    private SwipeRefreshLayout swipeRefreshLayout;

    //detailed view with webview for news
    public DetailNewsFragment(String link, SwipeRefreshLayout swipeRefreshLayout){
        this.link = link;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_compus, container, false);

        //Get a reference to your WebView//
        WebView webView = (WebView) view.findViewById(R.id.webview);

        //Specify the URL you want to display//
        webView.loadUrl(link);

        swipeRefreshLayout.setEnabled(false);

        return view;
    }
}
