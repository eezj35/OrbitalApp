package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User extends AppCompatActivity {

//    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> {startActivity(new Intent(this, MainActivity.class));});
    }

}