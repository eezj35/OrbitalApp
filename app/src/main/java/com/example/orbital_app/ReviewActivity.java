package com.example.orbital_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int numPpl = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        rv = findViewById(R.id.reviewRV);

        Bundle bundle = getIntent().getExtras();
        locName = bundle.getString("locName");

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ReviewsAdapter adapter = new ReviewsAdapter(ReviewActivity.this, list);
        db.collection("reviews").whereEqualTo("place", locName)
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
                Intent i = new Intent(ReviewActivity.this, LeaveReview.class);
                i.putExtra("locName", locName);

                startActivity(i);
            }
        });


    }

}