package com.rajaryan.splitit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer t=new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        },5000);
    }
}