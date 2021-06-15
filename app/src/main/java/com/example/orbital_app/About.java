package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class About extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {


                        case R.id.nav_home:
                            Toast.makeText(About.this, "Home Favourites", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;

                        case R.id.nav_favourites:
                            Toast.makeText(About.this, "Selected Favourites", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), FavList.class));
                            break;

                        case R.id.nav_search:
                            Toast.makeText(About.this, "Selected Search", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Search.class));
                            break;
                    }
                    return true;
                }
            };
}