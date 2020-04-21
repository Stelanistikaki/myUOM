package com.example.admin.myuom.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.admin.myuom.MainActivity;
import com.example.admin.myuom.Program.ProgramFragment;
import com.example.admin.myuom.R;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;


public class SettingsFragment extends Fragment {

    TextView aem, firstName, lastName, department, semester, direction;
    Spinner timeSpinner;
    Switch notificationSwitch;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] timeNotificationString = {"30 λεπτά", "1 ωρα", "2 ώρες"};

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        aem = (TextView) view.findViewById(R.id.aem);
        firstName = (TextView) view.findViewById(R.id.first_name);
        lastName = (TextView) view.findViewById(R.id.last_name);
        department = (TextView) view.findViewById(R.id.department);
        semester = (TextView) view.findViewById(R.id.semester);
        direction = (TextView) view.findViewById(R.id.direction);
        timeSpinner = view.findViewById(R.id.timeSpinner);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        //set the time notification spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, timeNotificationString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(spinnerArrayAdapter);

        // this = fragment
        SharedPreferences sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String id = sp.getString("id", "");
        //the id is known
        aem.setText(id);

        //notification switch for notifications on or off
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean("notifications", true).apply();
                    timeSpinner.setEnabled(true);
                }else{
                    sp.edit().putBoolean("notifications", false).apply();
                    //the user cannot choose time if there are not notifications
                    timeSpinner.setEnabled(false);
                }
            }
        });

        //set the shared preferences for notification time
        Boolean notificationsBool = sp.getBoolean("notifications", false);
        int timeNotification = sp.getInt("notificationTime", 0);
        //set the time according to what the user chose
        //get only the number and not the text ("minutes")
        if(timeNotification == 30){
            timeSpinner.setSelection(0);
        }else if(timeNotification == 1){
            timeSpinner.setSelection(1);
        }else{
            timeSpinner.setSelection(2);
        }
        //set the ui to respond to the user clicks
        if(notificationsBool){
            notificationSwitch.setChecked(true);
        }else {
            notificationSwitch.setChecked(false);
        }

        //the time spinner to choose the notification time
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iD) {
                String selectedtime = timeSpinner.getSelectedItem().toString();
                String[] s = selectedtime.split(" ");
                sp.edit().putInt("notificationTime", Integer.valueOf(s[0])).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get the data for the student from the database
        try {
            run(id, true, sp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void run(String id, boolean isClicked, SharedPreferences sp){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/student_info/"+ id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new Gson();

                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //create the Student object to show the data
                Student student = gson.fromJson(responseBody.string(), Student.class);
                sp.edit().putInt("semester", student.getSemester()).apply();
                sp.edit().putString("direction", student.getDirection()).apply();
                if(isClicked)
                    setView(student);
            }
        });
    }

    public void setView(Student student){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //set the views
                lastName.setText(student.getLastName());
                firstName.setText(student.getFirstName());
                String s = student.getDepartment();
                if (s.length() > 13) {
                    String str[] = s.split(" ");
                    department.setText(str[0] + "\n" + str[1]);
                } else
                    department.setText(s);
                semester.setText(String.valueOf(student.getSemester()));
                direction.setText(student.getDirection());
            }
        });
    }
}
