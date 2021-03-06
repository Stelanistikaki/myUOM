package com.example.admin.myuom.Compus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.admin.myuom.R;


public class CompusFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_compus, container, false);

        //Get a reference to your WebView//
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setBuiltInZoomControls(true);

        //Specify the URL you want to display//
        webView.loadUrl("https://compus.uom.gr/modules/auth/login.php");

        MyWebViewClient webViewClient = new MyWebViewClient();
        webView.setWebViewClient(webViewClient);

        return view;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        //Implement shouldOverrideUrlLoading//
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(Uri.parse(url).getHost().endsWith("compus.uom.gr")) {
                // will return ‘false” and the URL will be loaded inside your WebView
                return false;
            }

        //If the URL doesn’t contain this string, then it’ll return “true.” At this point, we’ll
        //launch the user’s preferred browser, by firing off an Intent//
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }

}


