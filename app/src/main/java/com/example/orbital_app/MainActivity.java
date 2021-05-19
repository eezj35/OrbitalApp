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

    private RecyclerView locationsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationsRecView = findViewById(R.id.locationsRV);

        ArrayList<Locations> locations = new ArrayList<>();

        locations.add(new Locations("MBS", "https://micdn-13a1c.kxcdn.com/images/sg/content-images/mbs.jpg"));
        locations.add(new Locations("GBTB", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("GBTB", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("GBTB", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
        locations.add(new Locations("GBTB", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));

        LocationsRVAdapter adapter = new LocationsRVAdapter(this);

        adapter.setLocations(locations);
        locationsRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        locationsRecView.setItemAnimator(new DefaultItemAnimator());
        locationsRecView.setAdapter(adapter);

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoggingIn.class));
        finish();
        //hello
    }
}