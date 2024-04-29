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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity{

    Button back_login, signup_bttn;
    EditText emaill, usernamee, passwordd;
    ProgressBar pBar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back_login = (Button) findViewById(R.id.gobacklogn_bttn);
        signup_bttn = (Button) findViewById(R.id.signupbttn);
        usernamee = (EditText) findViewById(R.id.reg_username);
        emaill = (EditText) findViewById(R.id.reg_email);
        passwordd = (TextInputEditText) findViewById(R.id.reg_pass);
        pBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        signup_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupbttn();
            }
        });

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signup.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }


    private void signupbttn() {
        String username = usernamee.getText().toString().trim();
        String email = emaill.getText().toString().trim();
        String password = passwordd.getText().toString().trim();


        if(username.isEmpty()){
            usernamee.setError("Username is Required!");
            usernamee.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emaill.setError("Email is Required!");
            emaill.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaill.setError("Please Provide Valid Email!");
            emaill.requestFocus();
            return;
        }
        if(password.isEmpty()){
            emaill.setError("Password is Required!");
            emaill.requestFocus();
            return;
        }
        if (password.length() < 8){
            emaill.setError("Min Password Length Should be a 8 Characters!");
            emaill.requestFocus();
            return;
        }

        pBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setPriority(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Toast.makeText(Signup.this, "User has been Registered Successfully!", Toast.LENGTH_LONG).show();
                                                pBar.setVisibility(View.GONE);
                                            }else{
                                                Toast.makeText(Signup.this, "Failed to Register! Try Again!", Toast.LENGTH_LONG).show();
                                                pBar.setVisibility(View.GONE);
                                            }

                                        }
                                    });
                        }else {
                            Toast.makeText(Signup.this, "Failed to Register!", Toast.LENGTH_LONG).show();
                            pBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}