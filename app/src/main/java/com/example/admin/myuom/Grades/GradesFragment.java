package com.example.admin.myuom.Grades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.admin.myuom.Program.Lesson;
import com.example.admin.myuom.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class GradesFragment extends Fragment{

    private ListView gradeList;
    private Spinner semesterSpinner;
    private String[] semesterString = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private String selectedSemester;
    private ArrayList<Lesson> lessons ;
    private ArrayList<Grade> grades;
    View view;
    String id, direction;

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
        direction = sp.getString("direction", "");
        int semester = sp.getInt("semester",0);
        semesterSpinner.setSelection(semester-1);


       // run(id, semester, direction);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iD) {
                selectedSemester = semesterSpinner.getSelectedItem().toString();
                run(id, Integer.valueOf(selectedSemester), direction);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void run(String username, int semester, String direction){
        OkHttpClient client = new OkHttpClient();
        lessons = new ArrayList<>();
        grades = new ArrayList<>();

        Request requestLessons = new Request.Builder()
                .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/lessons")
                .build();

        client.newCall(requestLessons).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try {
                    JSONObject obj = new JSONObject(responseBody.string());
                    JSONObject semObj = obj.getJSONObject(String.valueOf(semester));
                    JSONObject lessonObj = semObj.getJSONObject(direction);
                    for(int i=0; i<lessonObj.names().length();i++){
                        Lesson aLesson = new Lesson();
                        String id = lessonObj.names().get(i).toString();
                        aLesson.setId(id);
                        JSONObject lesson = lessonObj.getJSONObject(id);
                        aLesson.setName(lesson.getString("name"));
                        lessons.add(aLesson);
                    }
                    lessonObj = semObj.getJSONObject("ΕΠΔΤ");
                    for(int i=0; i<lessonObj.names().length();i++){
                        Lesson aLesson = new Lesson();
                        String id = lessonObj.names().get(i).toString();
                        aLesson.setId(id);
                        JSONObject lesson = lessonObj.getJSONObject(id);
                        aLesson.setName(lesson.getString("name"));
                        lessons.add(aLesson);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Request requestGrade = new Request.Builder()
                        .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/grades/"+ username)
                        .build();

                client.newCall(requestGrade).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(responseBody.string());

                            for(int i=0; i<obj.names().length();i++){
                                for(int j=0; j<lessons.size();j++){
                                    if(obj.names().get(i).toString().equals(lessons.get(j).getId())){
                                        Grade grade = new Grade();
                                        String id_lesson = obj.names().get(i).toString();
                                        JSONObject gradeobj = obj.getJSONObject(id_lesson);
                                        grade.setGrade(gradeobj.getString("grade"));
                                        grade.setName(lessons.get(j).getName());
                                        grades.add(grade);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomAdapterList adapter = new CustomAdapterList(getContext(), R.layout.fragment_grades_list, grades);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override public void run() {
                                gradeList.setAdapter(adapter);
                            }
                        });
                    }
                });

            }
        });

    }


}
