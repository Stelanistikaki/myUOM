package com.example.admin.myuom.Program;

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
import java.util.Calendar;


public class ProgramFragment extends Fragment {

    private ListView programList;
    private Spinner programSpinner;
    String id, direction;
    int semester;
    String dayOfWeek;
    String selectedDay,selectedDay2="",selectedDay3="";
    ArrayList<String> lessons;
    private String[] programString = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        programList = view.findViewById(R.id.program_list);
        programSpinner = view.findViewById(R.id.programSpinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, programString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programSpinner.setAdapter(spinnerArrayAdapter);

        //set the spinner in the current day
        Calendar calendar = Calendar.getInstance();
        dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        int selection = 0;
        switch (dayOfWeek) {
            case "ΔΕΥΤΕΡΑ":
                selectedDay = dayOfWeek;
                selection = 0;
                break;
            case "ΤΡΙΤΗ":
                selectedDay = dayOfWeek;
                selection = 1;
                break;
            case "ΤΕΤΑΡΤΗ":
                selectedDay = dayOfWeek;
                selection = 2;
                break;
            case "ΠΕΜΠΤΗ":
                selectedDay = dayOfWeek;
                selection = 3;
                break;
            case "ΠΑΡΑΣΚΕΥΗ":
                selectedDay = dayOfWeek;
                selection = 4;
                break;
            default: //the default is for Sunday and Saturday
                selection=0;
        }
        programSpinner.setSelection(selection);

        // this = your fragment
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        id = sp.getString("id", "");
        semester = sp.getInt("semester",0);
        direction = sp.getString("direction", "");


        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iD) {
                selectedDay = programSpinner.getSelectedItem().toString();
                if(selectedDay.equals("Δευτέρα")){
                    selectedDay = "ΔΕΥΤΕΡΑ";
                    selectedDay2 = "ΔΕΥΤΕΡΑ2";
                    selectedDay3 = "";
                }else if(selectedDay.equals("Τρίτη")){
                    selectedDay = "ΤΡΙΤΗ";
                    selectedDay2="";
                    selectedDay3 = "";
                }else if(selectedDay.equals("Τετάρτη")) {
                    selectedDay = "ΤΕΤΑΡΤΗ";
                    selectedDay2 = "ΤΕΤΑΡΤΗ2";
                    selectedDay3 = "ΤΕΤΑΡΤΗ3";
                }else if(selectedDay.equals("Πέμπτη")) {
                    selectedDay = "ΠΕΜΠΤΗ";
                    selectedDay2 = "";
                    selectedDay3 = "";
                }else if(selectedDay.equals("Παρασκευή")) {
                    selectedDay = "ΠΑΡΑΣΚΕΥΗ";
                    selectedDay2 = "";
                    selectedDay3 = "";
                }
                run(id, String.valueOf(semester), direction);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void run(String username, String semester, String direction){
        OkHttpClient client = new OkHttpClient();

        Request requestLessons = new Request.Builder()
                .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/lessons")
                .build();

        client.newCall(requestLessons).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                lessons = new ArrayList<String>();
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                JSONObject jsonObject = null;

                try {
                    JSONObject obj = new JSONObject(responseBody.string());
                    JSONObject semObj = obj.getJSONObject(String.valueOf(semester));
                    JSONObject directionObj = semObj.getJSONObject(direction);

                    for(int i=0; i<directionObj.names().length();i++){
                       lessons.add(directionObj.names().get(i).toString());
                    }
                    directionObj = semObj.getJSONObject("ΕΠΔΤ");
                    for(int i=0; i<directionObj.names().length();i++){
                        lessons.add(directionObj.names().get(i).toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        loadList(lessons);
                    }
                });
            }
        });

    }

    public void loadList(ArrayList<String> lessons){
        JSONObject obj = null;
        JSONObject jsonObject;
        try {
            obj = new JSONObject(loadJSONFromAsset(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Program> data = new ArrayList<>();
        try {
            JSONArray jsonArray = obj.getJSONArray("ΠΡΟΓΡΑΜΜΑ");

            Program theProgram ;
            for(int i=0; i < jsonArray.length(); i++) {
                for(int j=0; j < lessons.size();j++){
                    jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString(selectedDay).contains(lessons.get(j))){
                        if(!selectedDay.equals("") && !(jsonObject.getString(selectedDay).equals(""))) {
                            theProgram = new Program();
                            theProgram.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram.setTitle(jsonObject.getString(selectedDay));
                            data.add(theProgram);
                        }
                        else if(!selectedDay2.equals("") && !(jsonObject.getString(selectedDay2).equals(""))) {
                            Program theProgram2 = new Program();
                            theProgram2.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram2.setTitle(jsonObject.getString(selectedDay2));
                            data.add(theProgram2);
                        }
                        else if(!selectedDay3.equals("") && !(jsonObject.getString(selectedDay3).equals(""))) {
                            Program theProgram3 = new Program();
                            theProgram3.setTime(jsonObject.getString("ΩΡΑ"));
                            theProgram3.setTitle(jsonObject.getString(selectedDay3));
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
        int timeNotification = sp.getInt("notificationTime", 0);
        int min,hour;
        //the notifications should initialize ONLY if its the day of the week
        //the user might switch to another day but should not get notification
        if(selectedDay.equals(dayOfWeek) && notify){
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

        ProgramListAdapter adapter = new ProgramListAdapter(getContext(), R.layout.fragment_program_list, data);
        programList.setAdapter(adapter);
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
                day = "ΚΥΡΙΑΚΗ";
                break;
            case 2:
                day = "ΔΕΥΤΕΡΑ";
                break;
            case 3:
                day = "ΤΡΙΤΗ";
                break;
            case 4:
                day = "ΤΕΤΑΡΤΗ";
                break;
            case 5:
                day = "ΠΕΜΠΤΗ";
                break;
            case 6:
                day = "ΠΑΡΑΣΚΕΥΗ";
                break;
            case 7:
                day = "ΣΑΒΒΑΤΟ";
                break;
        }
        return day;
    }

}
