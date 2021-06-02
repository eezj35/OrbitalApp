package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView rv;
    ReviewsAdapter adapter;
    ArrayList<Reviews> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        rv = findViewById(R.id.reviewRV);
        list = new ArrayList<>();
        list.add(new Reviews("Justin", 5, "Great place", 4));
        adapter = new ReviewsAdapter(ReviewActivity.this, list);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        Button leaveReview = findViewById(R.id.leaveReview);
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewActivity.this, LeaveReview.class));
            }
        });

    }
}