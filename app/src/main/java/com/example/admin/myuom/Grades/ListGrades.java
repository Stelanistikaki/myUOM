package com.example.admin.myuom.Grades;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.R;
import com.example.admin.myuom.Settings.SettingsFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ListGrades extends Fragment {
    private String id;
    private int semester;
    private ListView gradeList;
    private ArrayList<Lesson> lessons;
    private ProgressBar gradesProgressBar;
    private TextView emptyTextGrades;

    //constructor
    public ListGrades(String id, int semester, ArrayList<Lesson> lessons){
        this.id = id;
        this.semester = semester;
        this.lessons = lessons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_grades, container, false);
        gradeList = view.findViewById(R.id.grades_list);
        gradesProgressBar = view.findViewById(R.id.grades_progress);
        emptyTextGrades = view.findViewById(R.id.emptyTextViewGrades);

        gradeList.setEmptyView(emptyTextGrades);
        run();

        return view;
    }

    public void run(){
        OkHttpClient client = new OkHttpClient();
        ArrayList<Grade> grades = new ArrayList<>();
        //after the list of the lessons is filled get the grades for the lessons in the Lesson list
        Request requestGrade = new Request.Builder().url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/grades/"+id).build();

        emptyTextGrades.setVisibility(View.GONE);
        gradesProgressBar.setVisibility(View.VISIBLE);

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
                            for(int j=0; j<lessons.size();j++){
                                if(lessons.get(j).getSemester() == semester){
                                    for(int i=0; i<obj.names().length();i++){
                                        //compare the id of lesson in the Lesson list and the response from the api call
                                        if(obj.names().get(i).toString().equals(lessons.get(j).getId())){
                                            Grade grade = new Grade();
                                            String id_lesson = obj.names().get(i).toString();
                                            grade.setGrade(obj.getString(id_lesson));
                                            grade.setName(lessons.get(j).getName());
                                            grades.add(grade);

                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                //set the adapter of the list
                if(!grades.isEmpty()){
                    GradesListAdapter adapter = new GradesListAdapter(getContext(), R.layout.grades_list_item, grades);
                    //not available in fragment so get the activity
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            gradesProgressBar.setVisibility(View.GONE);
                            emptyTextGrades.setVisibility(View.VISIBLE);
                            gradeList.setAdapter(adapter);
                        }
                    });
                }
            }
        });

    }
}
