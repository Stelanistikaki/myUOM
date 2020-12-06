package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.myuom.Settings.SettingsFragment;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordView, mUsernameView;
    private Button loginButton;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //shared boolean reference
        sp = getSharedPreferences("pref",MODE_PRIVATE);

        //false is the default value for booleans
        //if the user is logged then go to Main Activity
        if(sp.getBoolean("logged",false)) {
            goToMainActivity();
            finish();
        }

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        loginButton = findViewById(R.id.sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    String username = mUsernameView.getText().toString();
                    String password = mPasswordView.getText().toString();

                    try {
                        run(username, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(LoginActivity.this, "Δεν υπάρχει σύνδεση στο Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    //get the data from database with api call
    public void run(String username, String password) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://us-central1-myuom-f49f5.cloudfunctions.net/app/api/login/"+ username)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String thePassword = null;
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody.string());
                        thePassword = jsonResponse.getString("password");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String text;
                    //password from textview equals to password from database
                    if(password.equals(thePassword)){
                        text = "Επιτυχής σύνδεση! ";
                        //set the shared preference values for the other activities
                        sp.edit().putBoolean("logged",true).apply();
                        sp.edit().putString("id", username).apply();
                        //this has to be here to update the sharedpreferences if another user logs in
                        new SettingsFragment(null).run(sp.getString("id", ""), false, sp);
                        goToMainActivity();
                        finish();
                    }else{
                        //the credentials are wrong
                        text = "Tα στοιχεία σου είναι λάθος ";
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


