package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrefForm extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    DatabaseReference prefRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_form);

        getSupportActionBar().setTitle("Preference Settings");

        radioGroup = findViewById(R.id.radioGroup);

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                prefRef = rtdb.getReference("pref").child(currentUserId);
                prefRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prefRef.child("house").setValue(radioButton.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Toast.makeText(PrefForm.this, "Restarting app...", Toast.LENGTH_LONG).show();
                finish();
//                startActivity(new Intent(PrefForm.this, LoggingIn.class));

            }
        });

    }

}