package com.aman.qrsacnner;
//QR will be decoded using open source library from git-hub: https://github.com/journeyapps/zxing-android-embedded/tree/master/zxing-android-embedded
//Connection with db is through JTDS drivers.
//No rest api is called in this application
//This application is connected with MSSQL DB

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent mainIntent = new Intent(SplashScreen.this, login.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
    }
}
