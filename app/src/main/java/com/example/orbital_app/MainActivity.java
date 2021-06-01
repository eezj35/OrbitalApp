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

    SwipeRefreshLayout refreshLayout;
    ProgressDialog pd;


//    LocationsRVAdapter adapter;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private FirestoreRecyclerAdapter<Locations, LocationHolder> adapter;

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

//        Justin for reference
//        recommendedRecView = findViewById(R.id.recommendedRV);
//        topRatedRecView = findViewById(R.id.topRatedRV);
//        newRecView = findViewById(R.id.newRV);
//
//        locations = new ArrayList<Locations>();
//
//        locations.add(new Locations("Marina Bay Sands", "https://mfiles.alphacoders.com/593/593386.jpg",4));
//        locations.add(new Locations("Gardens By The Bay", "https://media.tacdn.com/media/attractions-splice-spp-674x446/08/c7/8f/98.jpg", 5));
//        locations.add(new Locations("Sentosa", "https://i1.wp.com/www.agoda.com/wp-content/uploads/2019/06/Resorts-World-Sentosa.jpg",5));
//
//        adapter = new LocationsRVAdapter(this);
//
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
//        FirestoreRecyclerOptions<Locations> options = new FirestoreRecyclerOptions.Builder<Locations>()
//                .setQuery(query, Locations.class)
//                .build();
//        adapter = new LocationFSAdapter(options);
//        adapter = new FirestoreRecyclerAdapter<Locations, LocationHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull Locations model) {
//                holder.name.setText(model.getName());
//                holder.image.setText(model.getImage());
//
//                holder.parent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(holder.parent.getContext(), Activity2.class);
//                        i.putExtra("location", model.getName());
//                        i.putExtra("image", model.getImage());
//                        i.putExtra("rating", model.getRating());
//                        holder.parent.getContext().startActivity(i);
//
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
//                return new LocationHolder(v);
//            }
//        };

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
        querying(query, "id", newList, newRecView);

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
                            if(order.equals("rating")){
                                Locations loc = dc.getDocument().toObject(Locations.class);
                                if(dc.getType() == DocumentChange.Type.ADDED && (loc.getRating() >= 4.0)){
                                    list.add(loc);
                                }
                            }
                            else if(dc.getType() == DocumentChange.Type.ADDED){
                                list.add(dc.getDocument().toObject(Locations.class));
                            }

                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        rv.setAdapter(adapter);
    }

//    class LocationHolder extends RecyclerView.ViewHolder{
//        TextView name;
//        TextView image;
//        CardView parent;
//        public LocationHolder(View itemView) {
//            super(itemView);
//            parent = itemView.findViewById(R.id.parent);
//            name = itemView.findViewById(R.id.txtName);
//            image = itemView.findViewById(R.id.image);
//        }
//    }
}