package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {
    private ListView gradeList;
    private Spinner semesterSpinner;
    private String[] semesterString = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private BackgroundWorkerGrades backgroundWorkerGrades;
    private String selectedSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        gradeList = findViewById(R.id.grades_list);
        semesterSpinner = findViewById(R.id.semesterSpinner);

        Intent received = getIntent();
        final String id_student = received.getStringExtra("name");
        String semester = received.getStringExtra("semester");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, semesterString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(spinnerArrayAdapter);
        semesterSpinner.setSelection((Integer.valueOf(semester))-1);

        backgroundWorkerGrades = new BackgroundWorkerGrades(GradesActivity.this);
        backgroundWorkerGrades.execute(id_student, semesterSpinner.getSelectedItem().toString());


        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSemester = semesterSpinner.getSelectedItem().toString();
                backgroundWorkerGrades = new BackgroundWorkerGrades(GradesActivity.this);
                backgroundWorkerGrades.execute(id_student, selectedSemester);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class BackgroundWorkerGrades extends AsyncTask<String, String, String> {

        Context context;
        BackgroundWorkerGrades(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String semester = strings[1];
            String method = "POST";
            String url = "http://192.168.2.7/myprograms/getGrades.php";
            String data = null;
            try {
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                        + URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8");
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
            ArrayList<Lesson> data = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = obj.getJSONArray("grades");

                for(int i=0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Lesson theLesson = new Lesson();
                    theLesson.setName(jsonObject.getString("name"));
                    theLesson.setGrade(jsonObject.getString("grade"));
                    data.add(theLesson);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            CustomAdapterList adapter = new CustomAdapterList(GradesActivity.this, R.layout.custom_list_item, data);
            gradeList.setAdapter(adapter);


        }
    }
}
