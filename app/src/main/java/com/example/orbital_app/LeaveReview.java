package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class LeaveReview extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RatingBar ratingBar;
    Button btn;
    EditText edtTxt;
    String locName;
    int userRating;


    FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference refData = rtdb.getReference("user").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

        getSupportActionBar().setTitle("Reviews");


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
                if(userRating==0){
                    Toast.makeText(LeaveReview.this, "Please leave a rating", Toast.LENGTH_SHORT).show();
                    return;
                }



                DatabaseReference dataToBeSet = rtdb.getReference("reviews").child(locName).child(user.getUid());
                dataToBeSet.setValue(true);

                Reviews review = new Reviews(user.getUid(), userRating, reviewText, 0, locName);

                db.collection("reviews").add(review);
                Toast.makeText(LeaveReview.this, "Thanks for your review!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });



    }

}