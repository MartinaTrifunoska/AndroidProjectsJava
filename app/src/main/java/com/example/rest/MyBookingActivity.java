package com.example.rest;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rest.adapters.BookingAdapter;
import com.example.rest.adapters.NewsAdapter;
import com.example.rest.models.Reservation;
import com.example.rest.models.Upload;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyBookingActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    TextView txtMessage;
    RecyclerView recyclerBookings;
    BookingAdapter adapter;
    List<Reservation> reservations;

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
        setContentView(R.layout.activity_my_bookings);

        init();

        if(mAuth.getCurrentUser() == null){
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText("You are not logged in!");
        }
        else if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("martina_trifunoska@hotmail.com")){
            showAllBookings();
        }
        else{
            showMyBookings();
        }

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.myBookings);
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
                        startActivity(new Intent(getApplicationContext(), BookingActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myBookings:
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
        reservations = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");
        txtMessage = findViewById(R.id.txtMessage);

        recyclerBookings = findViewById(R.id.recyclerBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(MyBookingActivity.this));
    }

    private void showMyBookings() {
        final String myEmail = mAuth.getCurrentUser().getEmail();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Reservation reservation = dataSnapshot.getValue(Reservation.class);
                        if(reservation.getEmail().equals(myEmail)){
                            reservations.add(0, reservation);
                        }
                    }
                    adapter = new BookingAdapter(MyBookingActivity.this, reservations);
                    adapter.updateData(reservations);
                    recyclerBookings.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyBookingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAllBookings() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Reservation reservation = dataSnapshot.getValue(Reservation.class);
                        reservations.add(0, reservation);
                    }
                    adapter = new BookingAdapter(MyBookingActivity.this, reservations);
                    adapter.updateData(reservations);
                    recyclerBookings.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyBookingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}