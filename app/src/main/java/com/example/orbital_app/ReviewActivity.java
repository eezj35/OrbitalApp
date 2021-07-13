package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ArrayList<Reviews> list = new ArrayList<>();;
    private String locName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private SwipeRefreshLayout refreshLayout;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Reviews");

        refreshLayout = findViewById(R.id.reviewRefresh);

        rv = findViewById(R.id.reviewRV);

        Bundle bundle = getIntent().getExtras();
        locName = bundle.getString("locName");

        DatabaseReference refData = rtdb.getReference("reviews").child(locName);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ReviewsAdapter adapter = new ReviewsAdapter(ReviewActivity.this, list);
        db.collection("reviews").whereEqualTo("place", locName).orderBy("upVote", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            Reviews review = dc.getDocument().toObject(Reviews.class);
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                review.setId(dc.getDocument().getId());
                                list.add(review);
                            }
                        }
                        adapter.notifyDataSetChanged();


                    }
                });

        rv.setAdapter(adapter);

        Button leaveReview = findViewById(R.id.leaveReview);
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.hasChild(user.getUid())){
                            Intent i = new Intent(ReviewActivity.this, LeaveReview.class);
                            i.putExtra("locName", locName);
                            startActivity(i);
                        }else{
                            Toast.makeText(ReviewActivity.this, "You have already left a review", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                db.collection("reviews").whereEqualTo("place", locName).orderBy("upVote", Query.Direction.DESCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null){
                                    Log.e("Firestore error", error.getMessage());
                                    return;
                                }

                                for(DocumentChange dc : value.getDocumentChanges()){
                                    Reviews review = dc.getDocument().toObject(Reviews.class);
                                    if(dc.getType() == DocumentChange.Type.ADDED){
                                        review.setId(dc.getDocument().getId());
                                        list.add(review);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

                refreshLayout.setRefreshing(false);
            }
        });

//        bottomNavigationView = findViewById(R.id.bottom_navigation_review);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        overridePendingTransition(0,0);
//                        break;
//
//                    case R.id.nav_favourites:
//
//                        startActivity(new Intent(getApplicationContext(), FavList.class));
//                        overridePendingTransition(0,0);
//                        break;
//
//                    case R.id.nav_search:
//                        startActivity(new Intent(getApplicationContext(), Search.class));
//                        overridePendingTransition(0,0);
//                        break;
//                }
//                return true;
//            }
//        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

}