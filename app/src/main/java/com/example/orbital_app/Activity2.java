package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Activity2 extends AppCompatActivity {

    private RecyclerView speclocationRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        speclocationRecView = findViewById(R.id.speclocationRV);

        SpecificLocRVAdapter adapter = new SpecificLocRVAdapter(this);

        Bundle bundle = getIntent().getExtras();
        Locations location = new Locations(bundle.getString("location"), bundle.getString("image"));

        adapter.setLocations(location);
        speclocationRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        speclocationRecView.setItemAnimator(new DefaultItemAnimator());
        speclocationRecView.setAdapter(adapter);
    }
}