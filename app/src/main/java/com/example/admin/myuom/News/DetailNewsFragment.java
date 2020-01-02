package com.example.admin.myuom.News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.admin.myuom.R;

import androidx.fragment.app.Fragment;

public class DetailNewsFragment extends Fragment {
    private String link;

    public DetailNewsFragment(String link){
        this.link = link;
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

        return view;
    }
}
