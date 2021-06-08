package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PrefForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_form);

        getSupportActionBar().setTitle("Preference Settings");
    }
}