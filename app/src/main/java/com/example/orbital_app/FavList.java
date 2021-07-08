package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavList extends AppCompatActivity {

    private RecyclerView rv;
    private FavAdapter adapter;
    private ArrayList<Locations> list = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    FirebaseDatabase rtdb = FirebaseDatabase.getInstance();

    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        getSupportActionBar().setTitle("Favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem mI = menu.getItem(1);
        mI.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        rv = findViewById(R.id.favRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        adapter = new FavAdapter(this, list);

        DatabaseReference favListRef = rtdb.getReference("favList").child(currentUserId);

        Query query = favListRef.orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Locations loc = ds.getValue(Locations.class);
                    list.add(loc);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv.setAdapter(adapter);

//        refresh = findViewById(R.id.favRefresh);
//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                list.clear();
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot ds: snapshot.getChildren()){
//                            Locations loc = ds.getValue(Locations.class);
//                            list.add(loc);
//
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                refresh.setRefreshing(false);
//            }
//        });


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

                        case R.id.nav_search:

                            startActivity(new Intent(getApplicationContext(), Search.class));
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