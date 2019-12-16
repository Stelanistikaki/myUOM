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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ProgramFragment extends Fragment {

    private ListView programList;
    private Spinner programSpinner;
    String id, direction;
    int semester;
    String selectedDay,selectedDay2="",selectedDay3="";
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

        // this = your fragment
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        id = sp.getString("id", "");
        semester = sp.getInt("semester",0);
        direction = sp.getString("direction", "");

        BackgroundWorkerProgram backgroundWorkerGrades = new BackgroundWorkerProgram(getContext());
        backgroundWorkerGrades.execute(id, String.valueOf(semester), direction);


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
                BackgroundWorkerProgram backgroundWorkerGrades = new BackgroundWorkerProgram(getContext());
                backgroundWorkerGrades.execute(id, String.valueOf(semester), direction);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
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

    class BackgroundWorkerProgram extends AsyncTask<String, String, String> {

        Context context;
        BackgroundWorkerProgram(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String semester = strings[1];
            String direction = strings[2];
            String method = "POST";
            String url = "http://192.168.2.4/myprograms/getLessons.php";
            String data = null;
            try {
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                        + URLEncoder.encode("direction", "UTF-8") + "=" + URLEncoder.encode(direction, "UTF-8") + "&"
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
            ArrayList<String> data = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = obj.getJSONArray("lessons");

                for(int i=0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    data.add(jsonObject.getString("id"));
                }

                loadList(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

}
