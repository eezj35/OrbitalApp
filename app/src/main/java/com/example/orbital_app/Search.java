package com.example.orbital_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import java.util.List;


public class Search extends AppCompatActivity {
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//    private FirestoreRecyclerAdapter<Locations, Search.LocationHolder> adapter;

    private SearchAdapter adapter;
    private RecyclerView searchRV;
    private ArrayList<Locations> locations= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        locations.add(new Locations("Marina Bay Sands", "https://mfiles.alphacoders.com/593/593386.jpg",4));
        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg", 5));
        locations.add(new Locations("Sentosa", "https://i1.wp.com/www.agoda.com/wp-content/uploads/2019/06/Resorts-World-Sentosa.jpg",5));

        searchRV = findViewById(R.id.searchRV);
        adapter = new SearchAdapter(locations);
        searchRV.setLayoutManager(new LinearLayoutManager(this));
        searchRV.setAdapter(adapter);

        EditText editText = findViewById(R.id.searchBar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
//        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        Query query = db.collection("places")
//                .orderBy("name");
//        FirestoreRecyclerOptions<Locations> options = new FirestoreRecyclerOptions.Builder<Locations>()
//                .setQuery(query, Locations.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<Locations, Search.LocationHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull Search.LocationHolder holder, int position, @NonNull Locations model) {
//                holder.name.setText(model.getName());
//                holder.image.setText(model.getImage());
//
//            }
//
//            @NonNull
//
//            @Override
//            public Search.LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
//                return new Search.LocationHolder(v);
//            }
//        };


    }

    private void filter(String text){
        ArrayList<Locations> filteredList = new ArrayList<>();

        for (Locations loc: locations){
            if(loc.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(loc);
            }
        }

        adapter.filterList(filteredList);
    }

//    public class LocationHolder extends RecyclerView.ViewHolder{
//        TextView name;
//        TextView image;
//
//        public LocationHolder(View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.searchName);
//            image = itemView.findViewById(R.id.searchImage);
//        }
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
