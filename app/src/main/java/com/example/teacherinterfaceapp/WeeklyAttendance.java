package com.example.teacherinterfaceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import com.example.teacherinterfaceapp.Students;

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
    Calendar startDateC;

    //datecheck type
    int start_or_end;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    String date;

    TreeMap<Integer,Integer> list;
    TreeMap<Integer,Students> namelist;
    TreeMap<Integer, Students> emailist;

    ArrayList<String> maillist = new ArrayList<>();
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

        list = new TreeMap<>();
        namelist = new TreeMap<>();
        emailist = new TreeMap<>();

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
                }
                else
                {
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    enddate_et.setText(sdf.format(myCalendar.getTime()));
                }

            }
        };


        startdate_et.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    start_or_end = 1;
                    DatePickerDialog dialog = new DatePickerDialog(WeeklyAttendance.this ,date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();

                } else
                    {

                }
            }
        });

        enddate_et.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    start_or_end = 2;
                    DatePickerDialog dialog = new DatePickerDialog(WeeklyAttendance.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                } else
                    {
                }

            }
        });
        getStudentData();

    }

    public void chechrecord(View view)
    {
        String sd = startdate_et.getText().toString();
        String ed = enddate_et.getText().toString();
        if (sd.equals("") || ed.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please choose Start Date and End Date", Toast.LENGTH_LONG).show();

        }
        else
        {
            Calendar cal = startDate;
            cal.add(Calendar.DATE,1);
            String  date = sdf.format(cal.getTime());

//            Log.d("MSGMSG",startdate_et.getText().toString()+"--"+enddate_et.getText().toString());
            if (date.equals(enddate_et.getText().toString()))
            {
                Log.d("MSGMSG",startdate_et.getText().toString()+"--"+enddate_et.getText().toString());

                int i = 0;
                startDateC=startDate;

                attendanceReference.orderByKey().startAt(startdate_et.getText().toString()).endAt(enddate_et.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotouter)
                    {
                        for (DataSnapshot dataSnapshot : dataSnapshotouter.getChildren())
                        {
                            Log.d("MSGMSG", dataSnapshot.getValue()+"");
                            Set set = list.keySet(); // saving the key value of student
                            final Iterator iterator = set.iterator();
                            for (DataSnapshot d:dataSnapshot.getChildren())
                            {
                                Log.d("MSGMSG1", dataSnapshot.getChildren()+"");

                                if (dataSnapshot.exists())
                                {
                                    while (iterator.hasNext())
                                    {
                                        int key = (int) iterator.next();
                                        if (dataSnapshot.hasChild(key+""))
                                        {
                                            int value = list.get(key);
                                            value=value+1;
                                            list.put(key,value);
                                        }
                                    }
                                }
                            }

                        }

                        showData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            else
                {
                Toast.makeText(getApplicationContext(),"Please Select Seven Days Difference",Toast.LENGTH_LONG).show();

            }
        }
    }



    public void getStudentData()
    {
        studentReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot d : dataSnapshot.getChildren())
                    {
                        Students obj = d.getValue(Students.class);
                        list.put(Integer.valueOf(obj.getStudentid()),0);
                        namelist.put(Integer.valueOf(obj.getStudentid()),obj);



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showData()
    {
//        classList.removeAllViews();
        Set set = list.keySet();
        Iterator iterator  = set.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            LinearLayout layout2 = new LinearLayout(getApplicationContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setWeightSum(10);

            int key = (int) iterator.next();

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,8));
            textView.setText(namelist.get(key).getName());
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextSize(22);

            layout2.addView(textView);

            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,2));
            textView1.setText(((int)((float)list.get(key)/2*100))+"%");
            textView1.setTextColor(Color.parseColor("#000000"));
            textView1.setTextSize(22);

            if (((int)((float)list.get(key)/2*100))<50)
            {
                layout2.addView(textView1);
                classList.addView(layout2);
                maillist.add(namelist.get(key).getEmail());
            }

        }
    }

public void sendmail(View view)
{
    Log.d("MSGMSG",maillist.toString());

    String mail[]=new String[maillist.size()];
    for (int i=0;i<maillist.size();i++)
    {
        mail[i]=maillist.get(i);
    }


    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL, maillist);
    i.putExtra(Intent.EXTRA_SUBJECT,"Low Attendance");
    i.putExtra(Intent.EXTRA_TEXT,"You are having low attendance, Kindly do meet me in class");

    try {
        startActivity(Intent.createChooser(i,"Send mail..."));

    }
    catch (android.content.ActivityNotFoundException e)
    {
        Toast.makeText(WeeklyAttendance.this,"No Student found ",Toast.LENGTH_SHORT).show();
    }
}


}
