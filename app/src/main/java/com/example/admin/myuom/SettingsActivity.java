package com.example.admin.myuom;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

public class SettingsActivity extends Activity {

    TextView aem,firstName, lastName, department, semester, direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        aem = (TextView)findViewById(R.id.aem);
        firstName = (TextView)findViewById(R.id.first_name);
        lastName = (TextView)findViewById(R.id.last_name);
        department = (TextView)findViewById(R.id.department);
        semester = (TextView)findViewById(R.id.semester);
        direction = (TextView)findViewById(R.id.direction);

        new GetProductDetails().execute();

    }

    class GetProductDetails extends AsyncTask<String, String, String> {
//        String url = "http://192.168.2.8/myprograms/getStudentInfo.php";
//        @Override
//        protected String doInBackground(String... strings) {
//            JSONObject json = jsonParser.makeHttpRequest(url, "GET", id);
//        }
    }

}
