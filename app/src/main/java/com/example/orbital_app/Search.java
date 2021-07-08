package com.example.orbital_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {

    private SearchAdapter adapter;
    private RecyclerView searchRV;
    private ArrayList<Locations> locations= new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search");

        searchRV = findViewById(R.id.searchRV);
        adapter = new SearchAdapter(locations);

        db.collection("places").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                locations.add(dc.getDocument().toObject(Locations.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

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
                searchRV.setLayoutManager(new LinearLayoutManager(Search.this));
                searchRV.setAdapter(adapter);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem mI = menu.getItem(2);
        mI.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }


    private void filter(String text){
        ArrayList<Locations> filteredList = new ArrayList<>();

        for (Locations loc: locations){
            if((loc.getName().toLowerCase().contains(text.toLowerCase())
            || loc.getState().toLowerCase().equals(text.toLowerCase())
            || loc.getGeneralLoc().toLowerCase().equals(text.toLowerCase())) && !text.equals("")){
                filteredList.add(loc);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0,0);
                            break;

                        case R.id.nav_favourites:

                            startActivity(new Intent(getApplicationContext(), FavList.class));
                            overridePendingTransition(0,0);
                            break;

                        case R.id.nav_user:

                            startActivity(new Intent(getApplicationContext(), User.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                            break;
                    }
                    return true;
                }
            };
}
