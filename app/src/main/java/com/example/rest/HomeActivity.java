package com.example.rest;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.rest.adapters.NewsAdapter;
import com.example.rest.adapters.ViewPagerAdapter;
import com.example.rest.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    TextView logOut;
    TabLayout tabLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigation;
    FirebaseAuth mAuth;
    NewsFeedFragment newsFeedFragment;
    GalleryFragment galleryFragment;

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
        setContentView(R.layout.activity_home);

        init();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(newsFeedFragment,"News");
        viewPagerAdapter.addFragment(galleryFragment,"Gallery");
        viewPager.setAdapter(viewPagerAdapter);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.home);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
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
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        if (mAuth.getCurrentUser() != null){
            logOut.setVisibility(View.VISIBLE);
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeActivity.this, "You are logged out!", Toast.LENGTH_SHORT).show();
                Intent moveToLogin = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(moveToLogin);
            }
        });
    }

    private void init() {
        newsFeedFragment = new NewsFeedFragment();
        galleryFragment = new GalleryFragment();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        mAuth = FirebaseAuth.getInstance();
        logOut = findViewById(R.id.logOut);
    }
}