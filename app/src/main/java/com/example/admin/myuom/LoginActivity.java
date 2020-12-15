package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.myuom.Settings.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private  FirebaseAuth mAuth;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                    String email = mUsernameView.getText().toString();
                    String password = mPasswordView.getText().toString();
                    authenticateUser(email, password);
                }else
                    Toast.makeText(LoginActivity.this, "Δεν υπάρχει σύνδεση στο Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void authenticateUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String text ;
                        if (task.isSuccessful()) {
                            //password from textview equals to password from database
                            text = "Επιτυχής σύνδεση! ";
                            //set the shared preference values for the other activities
                            sp.edit().putBoolean("logged",true).apply();
                            String id;
                            id = email.split("@")[0];
                            sp.edit().putString("id", id).apply();
                            //this has to be here to update the sharedpreferences if another user logs in
                            new SettingsFragment(null).run(sp.getString("id", ""), false, sp);
                            goToMainActivity();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            text = "Tα στοιχεία σου είναι λάθος ";
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


