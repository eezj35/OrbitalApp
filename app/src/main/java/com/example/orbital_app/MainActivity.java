package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecView;
    private RecyclerView topRatedRecView;
    private RecyclerView newRecView;
    //overlay for bottom navigation
    private BottomNavigationView bottomNavigationView;

    ArrayList<Locations> recommendedList = new ArrayList<>();
    ArrayList<Locations> topRatedList = new ArrayList<>();
    ArrayList<Locations> newList = new ArrayList<>();

    ProgressDialog pd;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();

    UserInfo userInfo;
    PrefActivities prefActivities;
    String location;
    String prefActivity1;
    String prefActivity2;
    String prefActivity3;
    private DatabaseReference userPrefRef = rtdb.getReference("pref").child(currentUserId);
    private DatabaseReference userPrefRefActivities = rtdb.getReference("pref").child(currentUserId).child("activities");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HangOuts");

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();

        userPrefRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newRecView = findViewById(R.id.newRV);
                newRecView.setHasFixedSize(true);
                newRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                userInfo = snapshot.getValue(UserInfo.class);
                if(userInfo != null){
                    location = userInfo.getHouse();
                    querying(db.collection("places"), "generalLoc", newList, newRecView);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userPrefRefActivities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recommendedRecView = findViewById(R.id.recommendedRV);
                recommendedRecView.setHasFixedSize(true);
                recommendedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                prefActivities = snapshot.getValue(PrefActivities.class);
                if(prefActivities!=null){
                   prefActivity1 = prefActivities.getActivity1();
                   prefActivity2 = prefActivities.getActivity2();
                   prefActivity3 = prefActivities.getActivity3();
                    querying(db.collection("places"), "name", recommendedList, recommendedRecView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        topRatedRecView = findViewById(R.id.topRatedRV);
        topRatedRecView.setHasFixedSize(true);
        topRatedRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        querying(db.collection("places"),"rating", topRatedList, topRatedRecView);


        //overlay for bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //Michael preferences API
//        gives file with all the pref in main_preferences.xml
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean test = prefs.getBoolean("test", true);
//        Toast.makeText(this, test + "", Toast.LENGTH_SHORT).show();
    }

    //overlay for bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.nav_favourites:
                            Toast.makeText(MainActivity.this, "Selected Favourites", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), FavList.class));
                            break;

                        case R.id.nav_search:
                            Toast.makeText(MainActivity.this, "Selected Search", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Search.class));
                            break;
                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                break;

            case R.id.filter:
                Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
                break;

            case R.id.user:
                startActivity(new Intent(this, User.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoggingIn.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void querying(Query query, String order, ArrayList<Locations> list, RecyclerView rv){

        FSDataAdapter adapter = new FSDataAdapter(MainActivity.this, list);
        query.orderBy(order, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            Locations loc = dc.getDocument().toObject(Locations.class);
                            if(order.equals("rating")){
                                if(dc.getType() == DocumentChange.Type.ADDED && (loc.getRating() >= 4.0)){
                                    list.add(loc);
                                }
                            }
                            else if(order.equals("generalLoc")){

                                if(dc.getType() == DocumentChange.Type.ADDED && location!=null){
                                    if((loc.getGeneralLoc().equals(location))) {
                                        list.add(loc);
                                    }
                                }

                            }
                            else{
                                if(dc.getType() == DocumentChange.Type.ADDED && prefActivities!=null){
                                    for(String s : loc.getActivities()){
                                        if(s.equals(prefActivity1) || s.equals(prefActivity2) || s.equals(prefActivity3)){
                                                list.add(loc);
                                                break;
                                        }
                                    }
                                }

                            }

                        }
                        adapter.notifyDataSetChanged();
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                });
        rv.setAdapter(adapter);

    }


}