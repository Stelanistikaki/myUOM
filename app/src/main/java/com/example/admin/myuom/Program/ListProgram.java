package com.example.admin.myuom.Program;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.myuom.Lesson;
import com.example.admin.myuom.Notification.AlarmReceiver;
import com.example.admin.myuom.Notification.NotificationScheduler;
import com.example.admin.myuom.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;

import androidx.fragment.app.Fragment;

public class ListProgram extends Fragment {
    private ListView programList;
    private String id;
    private int semester;
    private ArrayList<Lesson> lessons;
    private String day, day2, day3;
    private ProgressBar programProgressBar;
    private TextView emptyTextProgram;

    public ListProgram(String id, int semester, ArrayList<Lesson> lessons, int intDay){
        this.id = id;
        this.semester = semester;
        this.lessons = lessons;
        this.day = getDayOfWeek(intDay);
        getDay2Day3(day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_program, container, false);
        programList = view.findViewById(R.id.program_list);
        programProgressBar = view.findViewById(R.id.program_progress);
        emptyTextProgram = view.findViewById(R.id.emptyTextViewProgram);

        //if the list is empty show something else
        programList.setEmptyView(emptyTextProgram);

        run(semester, lessons);

        return view;
    }


    public void loadList(ArrayList<String> studentLessons){
        JSONObject obj = null;
        JSONObject jsonObject;
        try {
            //get the data from the ep7.json file in assets file
            obj = new JSONObject(loadJSONFromAsset(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Program> data = new ArrayList<>();
        try {
            JSONArray jsonArray = obj.getJSONArray("ΠΡΟΓΡΑΜΜΑ");
            Program theProgram ;
            for (int i = 0; i < jsonArray.length(); i++) {
                for(int j=0; j< studentLessons.size();j++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    //compare the data in the Lesson list with the data in the json file
                    if (jsonObject.getString(day).contains(studentLessons.get(j))) {
                        //check if the selected day has no lessons
                        if (!day.equals("") && !(jsonObject.getString(day).equals(""))) {
                            theProgram = new Program();
                            theProgram.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram.setTitle(jsonObject.getString(day));
                            data.add(theProgram);
                        }
                        //for each day (2 and 3)
                        else if (!day2.equals("") && !(jsonObject.getString(day2).equals(""))) {
                            Program theProgram2 = new Program();
                            theProgram2.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram2.setTitle(jsonObject.getString(day2));
                            data.add(theProgram2);
                        } else if (!day3.equals("") && !(jsonObject.getString(day3).equals(""))) {
                            Program theProgram3 = new Program();
                            theProgram3.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram3.setTitle(jsonObject.getString(day3));
                            data.add(theProgram3);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean notify = sp.getBoolean("notifications", false);
        //if the notification switch is on set the notification
        int timeNotification = sp.getInt("notificationTime", 0);
        int min,hour;
        //the notifications should initialize ONLY if its the day of the week
        //the user might switch to another day but should not get notification
        Calendar calendar = Calendar.getInstance();
        String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)-1);
        if(day.equals(dayOfWeek) && notify){
            for(int i=0;i<data.size();i++){
                String timeString = data.get(i).getTime();
                String s[] = timeString.split(":");
                if(timeNotification == 30){
                    min = Integer.valueOf(s[1])+30;
                    hour = Integer.valueOf(s[0])-1;
                }else{
                    hour = Integer.valueOf(s[0]) - timeNotification;
                    min = Integer.valueOf(s[1]);
                }
                NotificationScheduler notificationScheduler = new NotificationScheduler();
                //random but UNIQUE id
                notificationScheduler.setReminder(getActivity(), AlarmReceiver.class, hour, min, i);
            }
        }

        //set the list adapter
        ProgramListAdapter adapter = new ProgramListAdapter(getContext(), R.layout.fragment_program_list, data);
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                programProgressBar.setVisibility(View.GONE);
                emptyTextProgram.setVisibility(View.VISIBLE);
                programList.setAdapter(adapter);
            }
        });
    }

    public void run(int semester, ArrayList<Lesson> lessons){
        OkHttpClient client = new OkHttpClient();
        //after the list of the lessons is filled get the grades for the lessons in the Lesson list
        Request requestGrade = new Request.Builder().url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/grades/"+id).build();

        emptyTextProgram.setVisibility(View.GONE);
        programProgressBar.setVisibility(View.VISIBLE);

        client.newCall(requestGrade).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                ArrayList<String> studentLessons = new ArrayList<>();
                ResponseBody responseBody = response.body();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(responseBody.string());
                    for(int j=0; j<lessons.size();j++){
                        if(lessons.get(j).getSemester() == semester){
                            for(int i=0; i<obj.names().length();i++){
                                //compare the id of lesson in the Lesson list and the response from the api call
                                if(obj.names().get(i).toString().equals(lessons.get(j).getId())){
                                    String id_lesson = obj.names().get(i).toString();
                                    studentLessons.add(id_lesson);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadList(studentLessons);
            }
        });

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ep_7.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "ΔΕΥΤΕΡΑ";
                break;
            case 2:
                day = "ΤΡΙΤΗ";
                break;
            case 3:
                day = "ΤΕΤΑΡΤΗ";
                break;
            case 4:
                day = "ΠΕΜΠΤΗ";
                break;
            case 5:
                day = "ΠΑΡΑΣΚΕΥΗ";
                break;
        }
        return day;
    }

    private void getDay2Day3(String day){
        if(day.equals("ΔΕΥΤΕΡΑ")){
            day2 = "ΔΕΥΤΕΡΑ2";
            day3 = "";
        }else if(day.equals("ΤΕΤΑΡΤΗ")) {
            day2 = "ΤΕΤΑΡΤΗ2";
            day3 = "ΤΕΤΑΡΤΗ3";
        }else {
            day2 = "";
            day3 = "";
        }
    }

}
