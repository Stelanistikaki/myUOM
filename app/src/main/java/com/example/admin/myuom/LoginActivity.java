package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordView, mUsernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
    }

    public void OnLogin(View view) {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        BackgroundWorkerLogin backgroundWorker = new BackgroundWorkerLogin(this);
        backgroundWorker.execute(username, password);

        finish();
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
                intent = new Intent(context, SettingsActivity.class);
                intent.putExtra("id", username);
            } else {
                toast = Toast.makeText(context, "Tα στοιχεία σου είναι λάθος", Toast.LENGTH_SHORT);
                intent = new Intent(context, LoginActivity.class);
            }
            toast.show();
            context.startActivity(intent);

        }
    }
}


