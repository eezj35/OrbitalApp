package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecView;
//    private RecyclerView topRatedRecView;
//    private RecyclerView newRecView;
    ArrayList<Locations> locations;
//    LocationsRVAdapter adapter;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        recommendedRecView = findViewById(R.id.recommendedRV);
        db = FirebaseFirestore.getInstance();
        setUpRecyclerView();

//        recommendedRecView = findViewById(R.id.recommendedRV);
//        topRatedRecView = findViewById(R.id.topRatedRV);
//        newRecView = findViewById(R.id.newRV);
//        locations = new ArrayList<Locations>();
//
//        locations.add(new Locations("Marina Bay Sands", "https://mfiles.alphacoders.com/593/593386.jpg"));
//        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
//        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
//        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg"));
//
//        adapter = new LocationsRVAdapter(this);
////
//        adapter.setLocations(locations);
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

//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("present", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("absent", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Search Selected", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Search.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }



    private void setUpRecyclerView(){

        recommendedRecView.setHasFixedSize(true);
        recommendedRecView.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("places");
        FirestoreRecyclerOptions<Locations> options = new FirestoreRecyclerOptions.Builder<Locations>()
                .setQuery(query, Locations.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Locations, LocationHolder>(options) {
            @Override
            protected void onBindViewHolder(LocationHolder holder, int position, Locations model) {
                holder.name.setText(model.getName());
                holder.image.setText(model.getImage());


                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(holder.parent.getContext(), Activity2.class);
                        i.putExtra("location", model.getName());
                        i.putExtra("image", model.getImage());
                        holder.parent.getContext().startActivity(i);

                    }
                });
            }

            @NonNull
            @Override
            public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
                return new LocationHolder(v);
            }
        };

        recommendedRecView.setAdapter(adapter);

    }

//    private void loadrecyclerViewData() {
//
//        db.collection("Data").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        // after getting the data we are calling on success method
//                        // and inside this method we are checking if the received
//                        // query snapshot is empty or not.
//                        if (!queryDocumentSnapshots.isEmpty()) {
//                            // if the snapshot is not empty we are hiding our
//                            // progress bar and adding our data in a list.
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for (DocumentSnapshot d : list) {
//                                // after getting this list we are passing that
//                                // list to our object class.
//                                Locations loc = d.toObject(Locations.class);
//
//                                // and we will pass this object class
//                                // inside our arraylist which we have
//                                // created for recycler view.
//
//                            }
//                            // after adding the data to recycler view.
//                            // we are calling recycler view notifyDataSetChanged
//                            // method to notify that data has been changed in recycler view.
//                            adapter.notifyDataSetChanged();
//                        } else {
//                            // if the snapshot is empty we are
//                            // displaying a toast message.
//                            Toast.makeText(MainActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // if we do not get any data or any error we are displaying
//                // a toast message that we do not get any data
//                Toast.makeText(MainActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class LocationHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView image;
        CardView parent;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.txtName);
            this.image = itemView.findViewById(R.id.image);
            this.parent = itemView.findViewById(R.id.parent);
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