package com.example.rest;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class BookingSuccessful extends AppCompatActivity {
    TextView messageView;
    Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_successful);

        messageView = findViewById(R.id.message);
        goBack = findViewById(R.id.goBack);

        Intent bookingSuccessful = getIntent();
        String date = bookingSuccessful.getStringExtra("date");
        String time = bookingSuccessful.getStringExtra("time");
        String guests = bookingSuccessful.getStringExtra("guests");
        String table = bookingSuccessful.getStringExtra("table");

        String message = "Your table is booked for " + date + " at " + time + " for " + guests + " guests, " + table + ".";
        messageView.setText(message);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingSuccessful.this, HomeActivity.class));
                finish();
            }
        });
    }
}