package com.example.admin.myuom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    int SPLASH_TIME = 2000; //This is 2 seconds
    Animation animFadeout;
    private ImageView logoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        //splash screen set up

        logoView = findViewById(R.id.logoStartPage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade_out);
                logoView.startAnimation(animFadeout);
                Intent mySuperIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(mySuperIntent);
                finish();


            }
        }, SPLASH_TIME);
    }
}
