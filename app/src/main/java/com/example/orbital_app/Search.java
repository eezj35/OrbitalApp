package com.example.orbital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Search extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirestoreRecyclerAdapter<Locations, Search.LocationHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("places")
                .orderBy("name");
        FirestoreRecyclerOptions<Locations> options = new FirestoreRecyclerOptions.Builder<Locations>()
                .setQuery(query, Locations.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Locations, Search.LocationHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Search.LocationHolder holder, int position, @NonNull Locations model) {
                holder.name.setText(model.getName());
                holder.image.setText(model.getImage());

            }

            @NonNull

            @Override
            public Search.LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
                return new Search.LocationHolder(v);
            }
        };


    }

    public class LocationHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView image;

        public LocationHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.searchName);
            image = itemView.findViewById(R.id.searchImage);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                searchData(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//        return true;
//    }

//    private void searchData(String s) {
//        db.collection("places").whereEqualTo("name",s.toLowerCase())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for(DocumentSnapshot doc : task.getResult()){
//                            Locations loc = new Locations(doc.getString("name"), doc.getString("image"));
//                            locations.add(loc);
//                        }
//                        adapter = new CustomAdapter(Search.this, locations);
//                        mRecyclerView.setAdapter(adapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//    }
}
