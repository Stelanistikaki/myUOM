package com.example.admin.myuom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class OpenEclassFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    public OpenEclassFragment(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_openeclass, container, false);

        //Get a reference to WebView//
        WebView webView = (WebView) view.findViewById(R.id.webviewEclass);
        webView.getSettings().setJavaScriptEnabled(true);
        //load the page
        webView.loadUrl("https://openeclass.uom.gr/main/portfolio.php");

        swipeRefreshLayout.setEnabled(false);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        return view;
    }
}
