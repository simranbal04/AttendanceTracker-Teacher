package com.example.teacherinterfaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.teacherinterfaceapp.Teachers;

import java.io.FileReader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherLogin extends AppCompatActivity {
    //database references to firebase
    FirebaseDatabase database;
    DatabaseReference studentReference;

    EditText username_et, password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login);
        database = FirebaseDatabase.getInstance();
        Log.d("TAG", "onCreate: "+database.getReference("teachers").getKey());
        studentReference = database.getReference("teachers");

        //refer edittext from xml file
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
    }

    public void studentLogin(View view) {

        //when user clicks in login button get the value of username&password and store it in a variable
        final String username = username_et.getText().toString().trim();
        final String password = password_et.getText().toString().trim();

        //if username or password empty

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill up all Fields", Toast.LENGTH_LONG).show();

        } else {

            // make connection with database to get user information
            studentReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // if username is correct data will exist
                    if (dataSnapshot.exists()) {
                        Teachers student_data = dataSnapshot.getValue(Teachers.class);

                        if (student_data.getPassword().equalsIgnoreCase(password)) {
                            Intent in = new Intent(getApplicationContext(), TeacherDashboard.class);
                            in.putExtra("username", username);
                            startActivity(in);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Username", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}



