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
import com.google.firebase.auth.FirebaseAuth;

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

    ArrayList<Locations> recommendedList = new ArrayList<>();
    ArrayList<Locations> topRatedList = new ArrayList<>();
    ArrayList<Locations> newList = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;
    ProgressDialog pd;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                refreshLayout.setRefreshing(false);
            }
        });

        setUpRecyclerView();

        //Michael preferences API
//        gives file with all the pref in main_preferences.xml
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean test = prefs.getBoolean("test", true);
//        Toast.makeText(this, test + "", Toast.LENGTH_SHORT).show();
    }

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

            case R.id.search:
                Intent i = new Intent(this, Search.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpRecyclerView() {

        Query query = db.collection("places");

        recommendedRecView = findViewById(R.id.recommendedRV);
        recommendedRecView.setHasFixedSize(true);
        recommendedRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        topRatedRecView = findViewById(R.id.topRatedRV);
        topRatedRecView.setHasFixedSize(true);
        topRatedRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        newRecView = findViewById(R.id.newRV);
        newRecView.setHasFixedSize(true);
        newRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        querying(query, "name", recommendedList, recommendedRecView);
        querying(query, "rating", topRatedList, topRatedRecView);
        querying(query, "generalLoc", newList, newRecView);

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
                                if(dc.getType() == DocumentChange.Type.ADDED && (loc.getGeneralLoc().equals("South"))){
                                    list.add(loc);
                                }
                            }
                            else if(dc.getType() == DocumentChange.Type.ADDED){
                                list.add(dc.getDocument().toObject(Locations.class));
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