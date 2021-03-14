package com.example.rest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.example.rest.models.Reservation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    EditText selectDate, selectTime, specialRequest;
    RadioGroup numberGuests, tablePreference;
    RadioButton radioGuests, radioTable;
    Button bookTable;
    TextView txtNotice;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigation;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mAuth.getCurrentUser() != null){
            finishAffinity();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        init();

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = rightNow.get(Calendar.MINUTE);

        if(currentHour >= 8 && currentHour < 19){
            currentHour = currentHour + 1;
            txtNotice.setText("The earliest possible reservation can be for " + currentHour + ":" + currentMinutes + " today.");
        }
        else if(currentHour < 8){
            txtNotice.setText("The earliest possible reservation can be for 8:00 today.");
        }
        else if(currentHour >= 19){
            txtNotice.setText("The earliest possible reservation can be for 8:00 tomorrow.");
        }

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, R.style.DateTimePicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        selectDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this, R.style.DateTimePicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectTime.setText((hourOfDay + ":" + minute));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        bookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null){
                    bookingTable();
                }
                else{
                    Toast.makeText(BookingActivity.this, "You need to log in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookingActivity.this, MainActivity.class));
                }
            }
        });

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.booking);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.booking:
                        return true;
                    case R.id.myBookings:
                        startActivity(new Intent(getApplicationContext(), MyBookingActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

        progressBar = findViewById(R.id.progressBar);
        txtNotice = findViewById(R.id.txtNotice);

        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        selectDate.setInputType(InputType.TYPE_NULL);
        selectTime.setInputType(InputType.TYPE_NULL);
        numberGuests = findViewById(R.id.numberGuests);
        tablePreference = findViewById(R.id.tablePreference);
        specialRequest = findViewById(R.id.specialRequest);
        bookTable = findViewById(R.id.bookTable);
    }

    private void bookingTable(){
        String date = selectDate.getText().toString();
        String time = selectTime.getText().toString();
        String requests = specialRequest.getText().toString();
        String email = mAuth.getCurrentUser().getEmail();

        int guestsId = numberGuests.getCheckedRadioButtonId();
        radioGuests = findViewById(guestsId);

        int tableId = tablePreference.getCheckedRadioButtonId();
        radioTable = findViewById(tableId);

        Reservation reservation;

        if(!date.isEmpty() && !time.isEmpty() && radioGuests != null && radioTable != null){
            String guests = radioGuests.getText().toString();
            String table = radioTable.getText().toString();

            if(!requests.isEmpty()){
                reservation = new Reservation(date,time,guests,table,requests,email);
            }
            else{
                reservation = new Reservation(date,time,guests,table,email);
            }

            progressBar.setVisibility(View.VISIBLE);

            String reservationID = email.replace(".","") + "|" + date.replace("/","") + "|" + time.replace(":","");
            databaseReference.child(reservationID).setValue(reservation);
            Intent bookingSuccessful = new Intent(BookingActivity.this, BookingSuccessful.class);
            bookingSuccessful.putExtra("date", date);
            bookingSuccessful.putExtra("time", time);
            bookingSuccessful.putExtra("guests", guests);
            bookingSuccessful.putExtra("table", table);
            startActivity(bookingSuccessful);
            finish();
        }
        else{
            Toast.makeText(BookingActivity.this, "Fill in the fields!", Toast.LENGTH_SHORT).show();
        }
    }
}