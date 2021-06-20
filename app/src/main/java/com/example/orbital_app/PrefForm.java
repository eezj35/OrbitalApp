package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrefForm extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    RadioGroup radioGroup;
    RadioButton radioButton;
    public boolean isCheck = false;

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7,
    cb8, cb9, cb10, cb11, cb12, cb13, cb14;

    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    DatabaseReference prefRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_form);
        getSupportActionBar().setTitle("Preference Settings");
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        radioGroup = findViewById(R.id.radioGroup);

        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cb9 = findViewById(R.id.cb9);
        cb10 = findViewById(R.id.cb10);
        cb11 = findViewById(R.id.cb11);
        cb12 = findViewById(R.id.cb12);
        cb13 = findViewById(R.id.cb13);
        cb14 = findViewById(R.id.cb14);

        ArrayList<CheckBox> cbList = new ArrayList<>();

        cbList.add(cb1);
        cbList.add(cb2);
        cbList.add(cb3);
        cbList.add(cb4);
        cbList.add(cb5);
        cbList.add(cb6);
        cbList.add(cb7);
        cbList.add(cb8);
        cbList.add(cb9);
        cbList.add(cb10);
        cbList.add(cb11);
        cbList.add(cb12);
        cbList.add(cb13);
        cbList.add(cb14);


        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                prefRef = rtdb.getReference("pref").child(currentUserId);

                int i=0;
                for(CheckBox cb: cbList){
                    if(cb.isChecked()){
                        i++;
                    }
                }

                if(radioId==-1){
                    Toast.makeText(PrefForm.this, "Please choose your general area of residence!", Toast.LENGTH_SHORT).show();
                }

                else if(i!=3){
                    Toast.makeText(PrefForm.this, "Please choose 3 preferred activities!", Toast.LENGTH_SHORT).show();
                }else{
                    prefRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(radioId!=-1){
                                prefRef.child("house").setValue(radioButton.getText().toString());
                                prefRef.child("isCheck").setValue(true);
                                int j=1;
                                for(CheckBox cb : cbList){
                                    if(cb.isChecked()) {
                                        prefRef.child("activities").child("activity" + j).setValue(cb.getText());
                                        j++;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Toast.makeText(PrefForm.this, "Please close and reopen the app", Toast.LENGTH_SHORT).show();
                    finish();

                    isCheck = true;
                }

            }
        });

    }
}