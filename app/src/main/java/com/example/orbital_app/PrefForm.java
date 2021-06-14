package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;

public class PrefForm extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    CheckBox cb5;

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

        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);

        ArrayList<CheckBox> cbList = new ArrayList<>();

        cbList.add(cb1);
        cbList.add(cb2);
        cbList.add(cb3);
        cbList.add(cb4);
        cbList.add(cb5);



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
                        int i=1;
                        for(CheckBox cb: cbList){
                            if(cb.isChecked()){
                                prefRef.child("activities").child("activity"+i).setValue(cb.getText());
                                i++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Toast.makeText(PrefForm.this, "Please close and reopen the app", Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }

}