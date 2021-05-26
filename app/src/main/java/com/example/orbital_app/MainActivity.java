package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecView;
//    private RecyclerView topRatedRecView;
//    private RecyclerView newRecView;
//    ArrayList<Locations> locations;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    private FirebaseDatabase db = FirebaseDatabase.getInstance();
//    private DatabaseReference root = db.getReference().child("Locations")

    private FirestoreRecyclerAdapter<Locations, LocationHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();

//        recommendedRecView = findViewById(R.id.recommendedRV);
//        topRatedRecView = findViewById(R.id.topRatedRV);
//        newRecView = findViewById(R.id.newRV);
//
//        locations = new ArrayList<Locations>();
//
////        locations.add(new Locations("Marina Bay Sands", "https://mfiles.alphacoders.com/593/593386.jpg"));
////        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
////        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
////        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
////        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
//
//        adapter = new LocationsRVAdapter(this, locations);
//
////        adapter.setLocations(locations);
//        recommendedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        recommendedRecView.setItemAnimator(new DefaultItemAnimator());
//
//
//        topRatedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        topRatedRecView.setItemAnimator(new DefaultItemAnimator());
//
//
//        newRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        newRecView.setItemAnimator(new DefaultItemAnimator());
//
//
//        recommendedRecView.setAdapter(adapter);
//        topRatedRecView.setAdapter(adapter);
//        newRecView.setAdapter(adapter);



    }



    private void setUpRecyclerView(){

        Query query = db.collection("places").orderBy("name");
        FirestoreRecyclerOptions<Locations> options = new FirestoreRecyclerOptions.Builder<Locations>()
                .setQuery(query, Locations.class)
                .build();
//        adapter = new LocationFSAdapter(options);
        adapter = new FirestoreRecyclerAdapter<Locations, LocationHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull Locations model) {
                holder.name.setText(model.getName());
                holder.image.setText(model.getImageUrl());

                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(holder.parent.getContext(), Activity2.class);
                        i.putExtra("location", model.getName());
                        i.putExtra("image", model.getImageUrl());
                        holder.parent.getContext().startActivity(i);

                    }
                });
            }

            @NonNull

            @Override
            public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
                return new LocationHolder(v);
            }
        };

        recommendedRecView = findViewById(R.id.recommendedRV);
        recommendedRecView.setHasFixedSize(true);
        recommendedRecView.setLayoutManager(new LinearLayoutManager(this));
        recommendedRecView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }

    }

    private class LocationHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView image;
        CardView parent;
        public LocationHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            name = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.image);
        }
    }
//
//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(this, LoggingIn.class));
//        finish();
//        //hello
//    }
}