package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LeaveReview extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RatingBar ratingBar;
    Button btn;
    EditText edtTxt;
    String locName;
    int userRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);


        btn = findViewById(R.id.reviewSubmit);
        edtTxt = findViewById(R.id.reviewInput);


        Bundle bundle = getIntent().getExtras();
        locName = bundle.getString("locName");

        ratingBar = findViewById(R.id.userRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = (int) rating;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = edtTxt.getText().toString();
                Reviews review = new Reviews("Justin", userRating, reviewText, 0, locName);

                db.collection("reviews").add(review);
                Toast.makeText(LeaveReview.this, "Thanks for your review!", Toast.LENGTH_SHORT).show();
            }
        });



    }

}