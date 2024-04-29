package com.example.kiosk_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    Button back, resetbttn;
    EditText emailv;
    ProgressBar pgsBar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = (Button) findViewById(R.id.bckbttn);
        resetbttn = (Button) findViewById(R.id.reset_bttn);
        emailv = (EditText) findViewById(R.id.emailv);
        pgsBar = (ProgressBar) findViewById(R.id.progressbar3);
        mAuth = FirebaseAuth.getInstance();

        resetbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_bttn();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forgot_Password.this, LoginActivity.class);
                startActivity(i);
            }
        });



    }
    private void reset_bttn(){
        String email = emailv.getText().toString().trim();

        if (email.isEmpty()){
            emailv.setError("Email is Required!");
            emailv.requestFocus();
            return;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailv.setError("Please Provide Valid Email!");
            emailv.requestFocus();
            return;
        }
        pgsBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Forgot_Password.this, "Check your Email to Reset your Password!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Forgot_Password.this, "Try Again! Something Wrong Happened!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}