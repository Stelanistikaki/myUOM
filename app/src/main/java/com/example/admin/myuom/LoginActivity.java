package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.ContentHandler;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordView, mUsernameView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sp = getSharedPreferences("pref",MODE_PRIVATE);

        //false is the default value for booleans
        if(sp.getBoolean("logged",false)) {
            goToMainActivity();
            finish();
        }

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

//        try {
//
//            // Assume that Moovit app exists. If not, exception will occur
//
//            PackageManager pm = getPackageManager();
//
//            pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES);
//
//            String uri = "moovit://directions?dest_lat=40.63314&dest_lon=22.94937&dest_name=Εγνατία165&orig_lat=&orig_lon=&orig_name=&date=&partner_id=<YOUR_APP_NAME>";
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//
//            intent.setData(Uri.parse(uri));
//
//            startActivity(intent);
//
//        } catch (PackageManager.NameNotFoundException e) {
//
//            // Moovit not installed - send to store
//
//            String url = "https://app.appsflyer.com/id498477945?pid=DL&c=<YOUR_APP_NAME>";
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//
//            i.setData(Uri.parse(url));
//
//            startActivity(i);
//
//        }
    }

    public void OnLogin(View view) {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        BackgroundWorkerLogin backgroundWorker = new BackgroundWorkerLogin(this);
        backgroundWorker.execute(username, password);

        finish();
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }


    class BackgroundWorkerLogin extends AsyncTask<String, String, String> {

        Context context;
        String username;

        BackgroundWorkerLogin(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... strings) {
            username = strings[0];
            String password = strings[1];
            String method = "POST";
            String url = "http://192.168.2.4/myprograms/login.php";
            String data = null;
            try {
                data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Connector connector = new Connector();
            String result = connector.connect(method, url, data);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent;
            Toast toast;
            if (result.equals("1")) {
                toast = Toast.makeText(context, "Επιτυχής σύνδεση!", Toast.LENGTH_SHORT);
                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putString("id", username).apply();
                goToMainActivity();
            } else {
                toast = Toast.makeText(context, "Tα στοιχεία σου είναι λάθος", Toast.LENGTH_SHORT);
                intent = new Intent(context, LoginActivity.class);
            }
            toast.show();

        }
    }
}


