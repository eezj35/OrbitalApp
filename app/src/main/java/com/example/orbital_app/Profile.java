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
    Button moveToChangePW, chooseProfilePic;
    ImageView profilePic;
    FirebaseStorage storage;
    StorageReference mStorageReference;
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
        chooseProfilePic = findViewById(R.id.chooseProfilePic);
        storage = FirebaseStorage.getInstance();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
                //selected image to be uploaded
            }
        });

        chooseProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
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
                fullName = findViewById(R.id.fullName);
                UserInfoName userInfoName = snapshot.getValue(UserInfoName.class);
                fullName.setText(userInfoName.getUserName());
                UserProfilePic userProfilePic = snapshot.getValue(UserProfilePic.class);
                String imURI = userProfilePic.getURI();

//                Glide.with(Profile.this)
//                        .asBitmap()
//                        .load(imURI)
//                        .into(profilePic);
                Picasso.get().load(imURI).into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        mStorageReference = FirebaseStorage.getInstance().getReference().child(imageURI.toString());
//        mStorageReference.getFile(imageURI).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                profilePic.setImageURI();
//            }
//        });


    }

    private void uploadImage() {
        if (imageURI != null) {
//            String uri = UUID.randomUUID().toString();
//            String uriFinal = "images/" + uri;
            StorageReference reference = storage.getReference().child(imageURI.toString());
            FirebaseDatabase userName = FirebaseDatabase.getInstance();
            DatabaseReference refData = userName.getReference("user").child(user.getUid());

            reference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        refData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                refData.child("URI").setValue(imageURI);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(Profile.this, "Image successfully uploaded.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Something went wrong! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    //result of uri
                    if (result != null) {
                        profilePic.setImageURI(result);
                        imageURI = result;
                    }
                }
            });

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}