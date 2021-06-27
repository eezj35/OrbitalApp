package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText enterEmailET;
    private Button resetPasswordBtn;
    private ProgressBar progressBar3;
    private TextView loginTV;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Password Reset");

        enterEmailET = findViewById(R.id.enterEmailET);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        progressBar3 = findViewById(R.id.progressBar3);
        loginTV = findViewById(R.id.loginTV);

        auth = FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoggingIn.class));
            }
        });
    }

    private void resetPassword() {
        String email = enterEmailET.getText().toString().trim();

        if (email.isEmpty()) {
            enterEmailET.setError("Email is required");
            enterEmailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            enterEmailET.setError("Please provide a valid email");
            enterEmailET.requestFocus();
            return;
        }

        progressBar3.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ForgotPassword.this, "Please try again", Toast.LENGTH_SHORT).show();
                    progressBar3.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}