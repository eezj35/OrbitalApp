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
    private TextView emptytv;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private SwipeRefreshLayout refreshLayout;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        emptytv = findViewById(R.id.emptyReview);
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
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            Reviews review = dc.getDocument().toObject(Reviews.class);
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                review.setId(dc.getDocument().getId());
                                list.add(review);
                            }
                        }
                        if (!list.isEmpty()) {
                            emptytv.setVisibility(View.GONE);
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

                        if(!snapshot.exists() || !snapshot.hasChild(user.getUid())){
                            Intent i = new Intent(ReviewActivity.this, LeaveReview.class);
                            i.putExtra("locName", locName);
                            startActivity(i);
                        } else {
                            Toast.makeText(ReviewActivity.this, "Your review has been left", Toast.LENGTH_SHORT).show();
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


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

}