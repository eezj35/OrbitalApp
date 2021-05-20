package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {startActivity(new Intent(this, MainActivity.class));});
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.backButton:
//                startActivity(new Intent(this, MainActivity.class));
//                break;
//        }
//    }
}