package com.example.admin.myuom;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ProgramFragment extends Fragment {

    private ListView programList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        programList = view.findViewById(R.id.program_list);

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

            for(int i=0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Program theProgram = new Program();
                theProgram.setTime(jsonObject.getString("ΩΡΑ"));
                theProgram.setTitle(jsonObject.getString("ΔΕΥΤΕΡΑ"));
                theProgram.setTitle(jsonObject.getString("ΔΕΥΤΕΡΑ2"));
                data.add(theProgram);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProgramListAdapter adapter = new ProgramListAdapter(getContext(), R.layout.fragment_program_list, data);
        programList.setAdapter(adapter);

        return view;
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

}
