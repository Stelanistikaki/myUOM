package com.example.admin.myuom;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class SettingsFragment extends Fragment {

    TextView aem, firstName, lastName, department, semester, direction;
    EditText address;
    Spinner timeSpinner;
    Switch notificationSwitch;
    Button changeAddress;
    View view;
    SharedPreferences sp;

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
        address = view.findViewById(R.id.addressSetting);
        timeSpinner = view.findViewById(R.id.timeSpinner);
        changeAddress = view.findViewById(R.id.changeAddress);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, timeNotificationString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(spinnerArrayAdapter);

        // this = your fragment
        sp = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String id = sp.getString("id", "");
        String addressText = sp.getString("address", "");
        address.setText(addressText);


        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean("notifications", true).apply();
                }else{
                    sp.edit().putBoolean("notifications", false).apply();
                }
            }
        });

        Boolean notificationsBool = sp.getBoolean("notifications", false);

        if(notificationsBool){
            notificationSwitch.setChecked(true);
        }else {
            notificationSwitch.setChecked(false);
        }

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

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressString = address.getText().toString();
                sp.edit().putString("address", addressString).apply();

                address.setText(sp.getString("address", ""));
            }
        });

       BackgroundWorkerSettings backgroundWorker = new BackgroundWorkerSettings(getContext());
       backgroundWorker.execute(id);

        return view;
    }

    class BackgroundWorkerSettings extends AsyncTask<String, String, String> {

        Context context;

        BackgroundWorkerSettings(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String data = null;
            String method = "POST";
            String url = "http://192.168.2.7/myprograms/getStudentInfo.php";
            try {
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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

            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = obj.getJSONArray("student");

                jsonObject = jsonArray.getJSONObject(0);
                aem.setText(jsonObject.getString("id"));
                lastName.setText(jsonObject.getString("last_name"));
                firstName.setText(jsonObject.getString("first_name"));
                String s = jsonObject.getString("department");
                if(s.length()>13){
                    String str[] = s.split(" ");
                    department.setText(str[0]+"\n"+str[1]);
                }else
                    department.setText(s);
                semester.setText(String.valueOf(jsonObject.getInt("semester")));
                sp.edit().putInt("semester", jsonObject.getInt("semester")).apply();
                s = jsonObject.getString("direction");
                sp.edit().putString("direction", s).apply();
//                if(s.length()>13){
//                    String str[] = s.split(" ");
//                    direction.setText(str[0]+"\n"+str[1]);
//                }else
                    direction.setText(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
