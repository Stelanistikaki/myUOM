package com.example.admin.myuom;

import android.os.AsyncTask;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

class LessonsTask extends AsyncTask<Void, Void, ArrayList<Lesson>> {
    ArrayList<Lesson> lessons = new ArrayList<Lesson>();

    @Override
    protected ArrayList<Lesson> doInBackground(Void... voids) {
        //call the endpoint for the lessons
        OkHttpClient client = new OkHttpClient();
        Request requestLessons = new Request.Builder()
                .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/lessons")
                .build();

        Response response = null;
        try {
            response = client.newCall(requestLessons).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the response
        ResponseBody responseBody = response.body();
        if (!response.isSuccessful()) try {
            throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get each lesson from the response and add them in a list
        try {
            JSONObject lessonsObj = new JSONObject(responseBody.string());
            //for the lessons that are available get the values and make Lessons objects
            for (int i = 0; i < lessonsObj.names().length(); i++) {
                String id = lessonsObj.names().get(i).toString();
                JSONObject lesson = lessonsObj.getJSONObject(id);
                Lesson theLesson = new Lesson();
                theLesson.setId(id);
                theLesson.setName(lesson.getString("name"));
                theLesson.setSemester(lesson.getInt("semester"));
                lessons.add(theLesson);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        //return the list
        return lessons;
    }
}