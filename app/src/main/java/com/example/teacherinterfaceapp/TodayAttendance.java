package com.example.teacherinterfaceapp;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TodayAttendance extends AppCompatActivity {


    TextView date_tv;
    TextView todaystudents_tv;
    TextView presentstudents_tv;
    TextView absentstudents_tv;
    TextView attendancerate_tv;

    String todayDate;

    LinearLayout classlist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todayattendance);

        date_tv = (TextView) findViewById(R.id.date_tv);
        todaystudents_tv = (TextView) findViewById(R.id.todaystudents_tv);
        presentstudents_tv = (TextView) findViewById(R.id.presentstudents_tv);
        absentstudents_tv = (TextView) findViewById(R.id.absentsstudents_tv);
        attendancerate_tv = (TextView) findViewById(R.id.attendancerate_tv);


        // Date Fetch
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        todayDate = sdf.format(Calendar.getInstance().getTime());

        date_tv.setText("Date: " +" "  +todayDate);

    }

}

