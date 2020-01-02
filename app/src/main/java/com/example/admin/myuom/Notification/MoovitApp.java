package com.example.admin.myuom.Notification;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MoovitApp extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Assume that Moovit app exists. If not, exception will occur
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES);
            String uri = "moovit://directions?dest_lat=40.63314&dest_lon=22.94937&dest_name=Εγνατία165&orig_lat=&orig_lon=&orig_name=&date=&partner_id=<YOUR_APP_NAME>";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            // Moovit not installed - send to store
            String url = "https://app.appsflyer.com/id498477945?pid=DL&c=<YOUR_APP_NAME>";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }
    }
}
