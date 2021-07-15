package com.example.orbital_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomePage extends AppCompatActivity {

    Button moveToPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HangOuts");

        moveToPreferences = findViewById(R.id.moveToPreferences);

        moveToPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PrefForm.class));
                Toast.makeText(WelcomePage.this, "Moving to preferences", Toast.LENGTH_SHORT).show();
            }
        });
    }
}