package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class User extends AppCompatActivity {

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
        finishAffinity();
    }
}