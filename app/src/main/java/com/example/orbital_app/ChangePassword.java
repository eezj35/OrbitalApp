package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    EditText enterPassword1;
    EditText enterPassword2;
    FirebaseAuth auth;
    Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change my password");
        enterPassword1 = findViewById(R.id.enterPassword1);
        enterPassword2 = findViewById(R.id.enterPassword2);
        auth = FirebaseAuth.getInstance();
        changePassword = findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = enterPassword1.getText().toString();
                String password2 = enterPassword2.getText().toString();

                if (password1.equals(password2) && password1.length() >= 6) {
                    change(v);
                } else if (password1.length() < 6) {
                    Toast.makeText(ChangePassword.this, "Password length must be more than 6 characters long", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePassword.this, "Please enter the same new password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void change(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(enterPassword2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePassword.this, "Your password has been changed", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(ChangePassword.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}