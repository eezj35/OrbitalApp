package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class User extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setTitle("User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container2, new UserFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation_user);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem mI = menu.getItem(3);
        mI.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_favourites:

                        startActivity(new Intent(getApplicationContext(), FavList.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.nav_search:

                        startActivity(new Intent(getApplicationContext(), Search.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                        break;

                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);

    }
}