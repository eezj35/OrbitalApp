package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecView;
    private RecyclerView topRatedRecView;
    private RecyclerView newRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recommendedRecView = findViewById(R.id.recommendedRV);
        topRatedRecView = findViewById(R.id.topRatedRV);
        newRecView = findViewById(R.id.newRV);

        ArrayList<Locations> locations = new ArrayList<>();

        locations.add(new Locations("Marina Bay Sands", "https://mfiles.alphacoders.com/593/593386.jpg"));
        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));

        LocationsRVAdapter adapter = new LocationsRVAdapter(this);

        adapter.setLocations(locations);
        recommendedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recommendedRecView.setItemAnimator(new DefaultItemAnimator());
        recommendedRecView.setAdapter(adapter);

        topRatedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        topRatedRecView.setItemAnimator(new DefaultItemAnimator());
        topRatedRecView.setAdapter(adapter);

        newRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        newRecView.setItemAnimator(new DefaultItemAnimator());
        newRecView.setAdapter(adapter);

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoggingIn.class));
        finish();
        //hello
    }
}