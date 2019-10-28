package com.example.admin.myuom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingsActivity extends Activity {

    TextView aem, firstName, lastName, department, semester, direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        aem = (TextView) findViewById(R.id.aem);
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        department = (TextView) findViewById(R.id.department);
        semester = (TextView) findViewById(R.id.semester);
        direction = (TextView) findViewById(R.id.direction);

        Intent received = getIntent();
        String id = received.getStringExtra("id");
        BackgroundWorkerSettings backgroundWorker = new BackgroundWorkerSettings(this);
        backgroundWorker.execute(id);
    }


    class BackgroundWorkerSettings extends AsyncTask<String, String, String> {

        Context context;

        BackgroundWorkerSettings(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String data = null;
            String method = "POST";
            String url = "http://192.168.2.4/myprograms/getStudentInfo.php";
            try {
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Connector connector = new Connector();
            String result = connector.connect(method, url, data);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = null;

            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = obj.getJSONArray("student");

                jsonObject = jsonArray.getJSONObject(0);
                aem.setText(jsonObject.getString("id"));
                lastName.setText(jsonObject.getString("last_name"));
                firstName.setText(jsonObject.getString("first_name"));
                String s = jsonObject.getString("department");
                if(s.length()>13){
                    String str[] = s.split(" ");
                    department.setText(str[0]+"\n"+str[1]);
                }else
                    department.setText(s);
                semester.setText(String.valueOf(jsonObject.getInt("semester")));
                s = jsonObject.getString("direction");
                if(s.length()>13){
                    String str[] = s.split(" ");
                    direction.setText(str[0]+"\n"+str[1]);
                }else
                    direction.setText(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
