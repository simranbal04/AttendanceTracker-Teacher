package com.example.teacherinterfaceapp;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TakeAttendance extends AppCompatActivity {
    //database reference
    DatabaseReference textReference;
    String todayDate;
    ImageView qr_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        textReference = firebaseDatabase.getReference("attendancedata");

        qr_img = findViewById(R.id.qr_img);

        //get today's date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        todayDate = sdf.format(Calendar.getInstance().getTime());

        //check if QR-code is already generated
        textReference.child(todayDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text_qr = "";
                if (dataSnapshot.exists()) {
                    Log.d("hello", dataSnapshot.getValue() + "");

                    //show the QR already generated from database in text view
                    text_qr = dataSnapshot.getValue() + "";
                } else
                    {
                    Random r = new Random();
                    int random = r.nextInt(8000000 - 6000000) + 6000000;
                    textReference.child(todayDate).setValue(random + "");
                    text_qr = random + "";
                }
                generateQR(text_qr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void generateQR(String text) {
        QRGEncoder qrgEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, 200);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qr_img.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
        }

    }

    public  void doneclick(View view){

        finish();
    }
}
