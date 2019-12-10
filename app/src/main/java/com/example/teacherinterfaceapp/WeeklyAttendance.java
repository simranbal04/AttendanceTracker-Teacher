package com.example.teacherinterfaceapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeeklyAttendance extends AppCompatActivity {


    EditText startdate_et;
    EditText enddate_et;

    Button mailbutton;
    LinearLayout classList;

    // Database
    DatabaseReference attendanceReference; // will fetch daily attendance
    DatabaseReference studentReference;  // will fetch student data

    //Calendar

    Calendar myCalendar;
    Calendar startDate;
    Calendar endDate;

    //datecheck type
    int start_or_end;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklyattendance);


        startdate_et = (EditText) findViewById(R.id.startdate_et);
        enddate_et = (EditText) findViewById(R.id.enddate_et);
        mailbutton = (Button) findViewById(R.id.mailbutton);
        classList = (LinearLayout) findViewById(R.id.classlist);

        myCalendar = (Calendar) Calendar.getInstance();
        startDate = (Calendar) Calendar.getInstance();
        endDate = (Calendar) Calendar.getInstance();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        attendanceReference = firebaseDatabase.getReference("dailyattendance");
        studentReference = firebaseDatabase.getReference("students");


        //    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (start_or_end == 1)
                {
                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    startdate_et.setText(sdf.format(myCalendar.getTime()));
                } else
                {
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    enddate_et.setText(sdf.format(myCalendar.getTime()));
                }

            }
        };


        startdate_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    start_or_end = 1;
                    DatePickerDialog dialog = new DatePickerDialog(WeeklyAttendance.this ,date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();

                } else {

                }
            }
        });

        enddate_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    start_or_end = 2;
                    DatePickerDialog dialog = new DatePickerDialog(WeeklyAttendance.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                } else {
                }

            }
        });

    }

    public void chechrecord(View view)
    {
        
    }



}
