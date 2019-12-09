package com.example.teacherinterfaceapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Takeattendance extends AppCompatActivity {

    ImageView qr_img;
    String todayDate; // for date
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance);

        qr_img = (ImageView) findViewById(R.id.qr_img);

        // fetch today date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        todayDate = sdf.format(Calendar.getInstance().getTime());



    }
}
