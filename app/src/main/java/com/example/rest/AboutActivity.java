package com.example.rest;

import android.content.Intent;
import android.media.Image;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.facebook.appevents.codeless.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class AboutActivity extends FragmentActivity implements OnMapReadyCallback {
    ImageView header;
    BottomNavigationView bottomNavigation;
    FirebaseAuth mAuth;
    GoogleMap map;

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
        setContentView(R.layout.activity_about);

        mAuth = FirebaseAuth.getInstance();
        header = findViewById(R.id.header);
        Picasso.get().load(R.mipmap.header).into(header);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.about);
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
                        startActivity(new Intent(getApplicationContext(), MyBookingActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng Noa = new LatLng(41.1118546,20.7971714);
        map.addMarker(new MarkerOptions().position(Noa).title("NOA lounge bar"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Noa, 16));
    }
}