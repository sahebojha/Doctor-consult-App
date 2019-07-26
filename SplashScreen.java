package com.example.sahebojha.doctorconsult;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {

    private View mContentView;

    private boolean mVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);





        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                Intent intent;
                if(user != null) {
                    if(user.equals("Patient")){
                        intent = new Intent(getApplicationContext(), Dashboard.class);
                    }
                    else
                    {
                        intent = new Intent(getApplicationContext(), DashboardDoctor.class);
                    }
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 1000);



    }





}
