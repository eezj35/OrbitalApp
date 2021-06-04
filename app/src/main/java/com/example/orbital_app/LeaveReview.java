package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class LeaveReview extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn;
    EditText edtTxt;
    String locName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);


        btn = findViewById(R.id.reviewSubmit);
        edtTxt = findViewById(R.id.reviewInput);

        Bundle bundle = getIntent().getExtras();
        locName = bundle.getString("locName");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = edtTxt.getText().toString();
                Reviews review = new Reviews("Justin", 5, reviewText, 4);
                db.collection("places").document(locName).collection("reviews").add(review);
                startActivity(new Intent(LeaveReview.this, ReviewActivity.class));
            }
        });



    }

}