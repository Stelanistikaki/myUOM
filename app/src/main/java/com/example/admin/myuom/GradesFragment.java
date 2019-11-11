package com.example.admin.myuom;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class GradesFragment extends Fragment {

    private ListView gradeList;
    private Spinner semesterSpinner;
    private String[] semesterString = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private String selectedSemester;
    View view;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_grades, container, false);

        gradeList = view.findViewById(R.id.grades_list);
        semesterSpinner = view.findViewById(R.id.semesterSpinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, semesterString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(spinnerArrayAdapter);

        // this = your fragment
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        id = sp.getString("id", "");
        int semester = sp.getInt("semester",0);
        semesterSpinner.setSelection(semester-1);

        BackgroundWorkerGrades backgroundWorkerGrades = new BackgroundWorkerGrades(getContext());
        backgroundWorkerGrades.execute(id, semesterSpinner.getSelectedItem().toString());


        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iD) {
                selectedSemester = semesterSpinner.getSelectedItem().toString();
                BackgroundWorkerGrades backgroundWorkerGrades = new BackgroundWorkerGrades(getContext());
                backgroundWorkerGrades.execute(id, selectedSemester);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
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
            String url = "http://192.168.2.3/myprograms/getGrades.php";
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

            CustomAdapterList adapter = new CustomAdapterList(getContext(), R.layout.custom_list_item, data);
            gradeList.setAdapter(adapter);


        }
    }


}
