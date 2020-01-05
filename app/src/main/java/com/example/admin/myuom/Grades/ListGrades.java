package com.example.admin.myuom.Grades;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import androidx.fragment.app.Fragment;

public class ListGrades extends Fragment {
    private String id, direction;
    private int semester;
    private ListView gradeList;

    public ListGrades(String id, int semester, String direction){
        this.id = id;
        this.direction = direction;
        this.semester = semester;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_grades, container, false);
        gradeList = view.findViewById(R.id.grades_list);

        run();

        return view;
    }

    public void run(){
        OkHttpClient client = new OkHttpClient();
        ArrayList<Lesson> lessons = new ArrayList<>();
        ArrayList<Grade> grades = new ArrayList<>();

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
                        .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/grades/"+id)
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

                        CustomAdapterList adapter = new CustomAdapterList(getContext(), R.layout.grades_list_item, grades);
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
