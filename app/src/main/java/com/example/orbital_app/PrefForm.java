package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.HashMap;
import java.util.Map;

public class PrefForm extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    RadioGroup radioGroup;
    RadioButton radioButton;
    public boolean isCheck = false;

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7,
    cb8, cb9, cb10, cb11, cb12, cb13, cb14;

    private static final String cb1key = "cb1_key";
    private static final String cb2key = "cb2_key";
    private static final String cb3key = "cb3_key";
    private static final String cb4key = "cb4_key";
    private static final String cb5key = "cb5_key";
    private static final String cb6key = "cb6_key";
    private static final String cb7key = "cb7_key";
    private static final String cb8key = "cb8_key";
    private static final String cb9key = "cb9_key";
    private static final String cb10key = "cb10_key";
    private static final String cb11key = "cb11_key";
    private static final String cb12key = "cb12_key";
    private static final String cb13key = "cb13_key";
    private static final String cb14key = "cb14_key";

    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    DatabaseReference prefRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();
    SharedPreferences sharedPreferences = null;



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
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cb9 = findViewById(R.id.cb9);
        cb10 = findViewById(R.id.cb10);
        cb11 = findViewById(R.id.cb11);
        cb12 = findViewById(R.id.cb12);
        cb13 = findViewById(R.id.cb13);
        cb14 = findViewById(R.id.cb14);

        Map<String, CheckBox> checkboxMap = new HashMap();
        checkboxMap.put(cb1key, cb1);
        checkboxMap.put(cb2key, cb2);
        checkboxMap.put(cb3key, cb3);
        checkboxMap.put(cb4key, cb4);
        checkboxMap.put(cb5key, cb5);
        checkboxMap.put(cb6key, cb6);
        checkboxMap.put(cb7key, cb7);
        checkboxMap.put(cb8key, cb8);
        checkboxMap.put(cb9key, cb9);
        checkboxMap.put(cb10key, cb10);
        checkboxMap.put(cb11key, cb11);
        checkboxMap.put(cb12key, cb12);
        checkboxMap.put(cb13key, cb13);
        checkboxMap.put(cb14key, cb14);

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

        sharedPreferences = getSharedPreferences("pref.form.checklist", Context.MODE_PRIVATE);

        final SharedPreferences preferences = getSharedPreferences("saved", 0);
        radioGroup.check(preferences.getInt("CheckedId", 0));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("CheckedId", checkedId);
                editor.apply();
            }
        });


        loadInitialValues(checkboxMap);
        setupCheckedChangeListener(checkboxMap);

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
                    Toast.makeText(PrefForm.this, "Preferences registered!", Toast.LENGTH_SHORT).show();
                    Intent intent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    isCheck = true;
                }

            }
        });

    }

    public void loadInitialValues(Map<String, CheckBox> checkboxMap) {
        for (Map.Entry<String, CheckBox> checkboxEntry: checkboxMap.entrySet()) {
            Boolean checked = sharedPreferences.getBoolean(checkboxEntry.getKey(), false);
            checkboxEntry.getValue().setChecked(checked);
        }
    }

    public void setupCheckedChangeListener(Map<String, CheckBox> checkboxMap) {
        for (final Map.Entry<String, CheckBox> checkboxEntry: checkboxMap.entrySet()) {
            checkboxEntry.getValue().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(checkboxEntry.getKey(), isChecked);
                    editor.apply();
                }
            });
        }
    }
}