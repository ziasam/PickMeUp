package com.example.pickmeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverActivitylogin extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;
    public EditText mEmail, mPassword;
    public Button mLogin, mRegister;
    public ProgressBar mprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_activitylogin);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(DriverActivitylogin.this, DriversMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mLogin = findViewById(R.id.Login);
        mRegister = findViewById(R.id.Register);
        mprogressbar = findViewById(R.id.progressbarId);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    mEmail.setError("Enter an email address");
                    mEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    mEmail.setError("Enter a valid email address");
                    mEmail.requestFocus();
                    return;
                }

                //checking the validity of the password

                if(password.isEmpty())
                {
                    mPassword.setError("Enter a password");
                    mPassword.requestFocus();
                    return;
                }

                if(password.length()<6)
                {
                    mPassword.setError("6 character or above");
                    mPassword.requestFocus();
                    return;
                }
                mprogressbar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverActivitylogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mprogressbar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Not Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id).child("name");
                            current_user_db.setValue(email);
                        }
                    }
                });
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    mEmail.setError("Enter an email address");
                    mEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    mEmail.setError("Enter a valid email address");
                    mEmail.requestFocus();
                    return;
                }

                //checking the validity of the password

                if(password.isEmpty())
                {
                    mPassword.setError("Enter a password");
                    mPassword.requestFocus();
                    return;
                }

                if(password.length()<6)
                {
                    mPassword.setError("6 character or above");
                    mPassword.requestFocus();
                    return;
                }
                mprogressbar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverActivitylogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mprogressbar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
