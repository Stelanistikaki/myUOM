package com.example.admin.myuom.Grades;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;

import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.R;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GradesTask extends AsyncTask<Void, Void, ArrayList<Lesson>> {
    private String id;
    ArrayList<Lesson> lessons = new ArrayList<Lesson>();
    private int semester;

    public GradesTask(String id, ArrayList<Lesson> lessons, int semester){
        this.id = id;
        this.semester = semester;
        this.lessons = lessons;
    }

    @Override
    protected ArrayList<Lesson> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        ArrayList<Lesson> unpassedLessons = new ArrayList<>();
        //after the list of the lessons is filled get the grades for the lessons in the Lesson list
        Request requestGrade = new Request.Builder().url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/grades/"+id).build();

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
                        for(int i=0; i<obj.names().length();i++){
                            //compare the id of lesson in the Lesson list and the response from the api call
                            if(obj.names().get(i).toString().equals(lessons.get(j).getId())){
                                Grade grade = new Grade();
                                String id_lesson = obj.names().get(i).toString();
                                grade.setGrade(obj.getString(id_lesson));
                                if(grade.getGrade().equals("-") || Integer.parseInt(grade.getGrade()) < 5 )
                                    if(!unpassedLessons.contains(lessons.get(j)))
                                        unpassedLessons.add(lessons.get(j));
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return unpassedLessons;
    }
}