package com.example.teacherinterfaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboard extends AppCompatActivity {
    TextView name_tv, date_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);
        name_tv = findViewById(R.id.name_tv);
        date_tv = findViewById(R.id.date_tv);


    //get today's date
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    String todayDate = sdf.format(Calendar.getInstance().getTime());


    date_tv.setText(todayDate);
    String username = getIntent().getStringExtra("username");
    name_tv.setText("User:"+username);
}

public void takeattendance(View view){
    startActivity(new Intent(getApplicationContext(),TakeAttendance.class));
}
public void viewattendance(View view){
    startActivity(new Intent(getApplicationContext(),Viewattendance.class));
}
}
