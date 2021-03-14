package com.example.rest;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FirebaseAuth mAuth;
    ImageSlider menuImages;

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
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        menuImages = findViewById(R.id.menuImages);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.mipmap.menu1));
        slideModels.add(new SlideModel(R.mipmap.menu2));
        slideModels.add(new SlideModel(R.mipmap.menu3));
        slideModels.add(new SlideModel(R.mipmap.menu4));
        slideModels.add(new SlideModel(R.mipmap.menu5));
        slideModels.add(new SlideModel(R.mipmap.menu6));
        slideModels.add(new SlideModel(R.mipmap.menu7));
        menuImages.setImageList(slideModels, false);


        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.menu);
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
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}