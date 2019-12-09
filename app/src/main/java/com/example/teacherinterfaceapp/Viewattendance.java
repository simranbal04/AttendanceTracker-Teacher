package com.example.teacherinterfaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Viewattendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewattendance);
    }

    public void todayattendance(View view)
    {
        Intent intent = new Intent(getApplicationContext(),TodayAttendance.class);
        startActivity(intent);
    }

    public void weekelyattendance(View view)
    {
        Intent intent = new Intent(getApplicationContext(),WeeklyAttendance.class);
        startActivity(intent);
    }
}
