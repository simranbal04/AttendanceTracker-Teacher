package com.example.teacherinterfaceapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

public class TodayAttendance extends AppCompatActivity {


    TextView date_tv;
    TextView todaystudents_tv;
    TextView presentstudents_tv;
    TextView absentstudents_tv;
    TextView attendancerate_tv;

    TreeMap<Integer, Boolean> list;
    TreeMap<Integer, Students> namelist;
    TreeMap<Integer, Students> emaillist;



    String todayDate;
    DatabaseReference attendanceReference, studentsReference;
    LinearLayout classlist;

    String maillist[];

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


        classlist = (LinearLayout) findViewById(R.id.classlist);
        list = new TreeMap<>();
        namelist = new TreeMap<>();
        emaillist = new TreeMap<>();

        // Date Fetch
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        todayDate = sdf.format(Calendar.getInstance().getTime());

        date_tv.setText("Date: " +" "  +todayDate);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        attendanceReference = firebaseDatabase.getReference("dailyattendance");
        studentsReference = firebaseDatabase.getReference("students");


        studentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Students obj = d.getValue(Students.class);
                        list.put(Integer.valueOf(obj.getStudentid()), false);
                        namelist.put(Integer.valueOf(obj.getStudentid()), obj);
//                        maillist.put(obj.getEmail(),obj);
//                        final Students put = maillist.put(maillist.getEmail());
//                        list.put(obj.getStudentid(),true);
//                        textview.setText(namelist.get(key).getName());



                    }
                    fetchAttendance();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void fetchAttendance() {
        attendanceReference.child(todayDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int present = 0;
                int absent = list.size();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        list.put(Integer.parseInt(d.getKey()), true);
//                        emaillist.put(d.getKey(),true);
                        present++;
                        absent--;
                    }
                }

                maillist=new String[absent];

//                maillist = new String[absent];

// data to be shown in textfields

                todaystudents_tv.setText("Total Students:  " + list.size());
                presentstudents_tv.setText("Present:  " + present);
                absentstudents_tv.setText("Absent:  " + (list.size() - present));

                if(list.size()!=0) {
                    int attendancerate = present / list.size();
                    attendancerate_tv.setText("Attendance Rate:  " + (int)(((float)present/list.size())*100)+"%");
                    generateList();

                }
                else
                {
                    TodayAttendance.this.finish();
                    Toast.makeText(getApplicationContext(),"Attendance not Marked Yet",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void generateList() {

        Set set = list.keySet(); // saving the key value of student list
        Iterator iterator = set.iterator();

        int i=0;
        while (iterator.hasNext()) { // gives next data from list
            LinearLayout layout2 = new LinearLayout(getApplicationContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setWeightSum(10);


            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(new LinearLayout.LayoutParams(0,
                    100, 1));


            int key = (int) iterator.next();
            if (list.get(key)) {
                imageView.setImageResource(R.drawable.ic_check_black_24dp);

            } else {
                imageView.setImageResource(R.drawable.ic_close_black_24dp);
                maillist[i]=namelist.get(key).getEmail();

                i++;
            }

            TextView textview = new TextView(this);

            textview.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 9));
            textview.setText(namelist.get(key).getName());
            textview.setTextColor(Color.parseColor("#000000"));
            textview.setTextSize(22);

            //adding view to layout
            layout2.addView(textview);

            layout2.addView(imageView);
            classlist.addView(layout2);
        }

    }

    public void sendmail(View view )
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, maillist);
        i.putExtra(Intent.EXTRA_SUBJECT,"Absent in Today's Class");
        i.putExtra(Intent.EXTRA_TEXT,"Please reply back with reason of your absence in class");

        try {
            startActivity(Intent.createChooser(i,"Send mail..."));

        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(TodayAttendance.this,"No Student found ",Toast.LENGTH_SHORT).show();
        }

    }


}

