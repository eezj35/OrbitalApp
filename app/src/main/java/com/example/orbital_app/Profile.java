package com.example.orbital_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView fullName, email;
    Button moveToChangePW;
    ImageView profilePic;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User Profile");

        FirebaseDatabase userName = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference refData = userName.getReference("user").child(user.getUid());
        moveToChangePW = findViewById(R.id.moveToChangePW);
        profilePic = findViewById(R.id.profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        moveToChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            }
        });

        //setting email
        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        refData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfoName userInfoName = snapshot.getValue(UserInfoName.class);
                fullName = findViewById(R.id.fullName);

                fullName.setText(userInfoName.getUserName());
                String imURI = userInfoName.getURI();
                Glide.with(Profile.this)
                        .asBitmap()
                        .load(imURI)
                        .into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    //result of uri
                    if (result != null) {
                        profilePic.setImageURI(result);
                        imageURI = result;
                        FirebaseDatabase userName = FirebaseDatabase.getInstance();
                        DatabaseReference refData = userName.getReference("user").child(user.getUid());
                        refData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                refData.child("URI").setValue(imageURI.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            });

}