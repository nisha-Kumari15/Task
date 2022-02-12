package com.example.task;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailSignIn, PasswordSignIn;
    TextView forgotPassword, newAcc;
    Button signIn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        emailSignIn = findViewById(R.id.emailSignIn);
        PasswordSignIn = findViewById(R.id.PasswordSignIn);
        forgotPassword = findViewById(R.id.forgotPassword);
        newAcc = findViewById(R.id.donthaveanAccount);
        signIn = findViewById(R.id.SignIn);
        progressBar = findViewById(R.id.progressBarSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUers();
            }
        });
    }

    private void loginUers() {

        String emailId = emailSignIn.getText().toString().trim();
        String pass = PasswordSignIn.getText().toString().trim();


        if (emailId.isEmpty()){
            emailSignIn.setError("Email is required!");
            emailSignIn.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            emailSignIn.setError("Please provide valid email");
            emailSignIn.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            PasswordSignIn.setError("Password is required!");
            PasswordSignIn.requestFocus();
            return;
        }

        if (pass.length() <= 8){
            PasswordSignIn.setError("Minimum password length should be 8 characters!");
            PasswordSignIn.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser.isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                    }else {
                        progressBar.setVisibility(View.GONE);
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your inbox, to verify your email.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}