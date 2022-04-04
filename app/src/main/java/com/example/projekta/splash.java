package com.example.projekta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Preference.getLoggedInStatus(getBaseContext()) == true){
                    Intent intent = new Intent(splash.this, Pointing.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(splash.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
    }
