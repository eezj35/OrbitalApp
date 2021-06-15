package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggingIn extends AppCompatActivity implements View.OnClickListener{

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, mForgotBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_in);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Log in");

        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.mLoginBtn);
        mCreateBtn = findViewById(R.id.mCreateBtn);
        mForgotBtn = findViewById(R.id.mForgotBtn);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mLoginBtn.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
        mForgotBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLoginBtn:

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (task.isSuccessful()) {

                            if (user.isEmailVerified()) {
                                Toast.makeText(LoggingIn.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(LoggingIn.this, "Check your email for authentication", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(LoggingIn.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;

            case R.id.mCreateBtn:
                startActivity(new Intent(getApplicationContext(), Registration.class));
                break;

            case  R.id.mForgotBtn:
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                break;

        }
    }
}