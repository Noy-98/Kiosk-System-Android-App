package com.example.kiosk_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewOnReceiveContentListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class LoginActivity extends AppCompatActivity{

    Button signup_button, login_bttn, forgot_passwordd;
    EditText l_email, l_pass;
    ProgressBar pbBar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup_button = (Button) findViewById(R.id.signupbttn);
        login_bttn = (Button) findViewById(R.id.loginbttn);
        forgot_passwordd = (Button) findViewById(R.id.forgotpass);
        l_email = (EditText) findViewById(R.id.login_email);
        l_pass = (EditText) findViewById(R.id.login_password);
        pbBar = (ProgressBar) findViewById(R.id.progressbar2);
        mAuth = FirebaseAuth.getInstance();

        login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbttn();
            }
        });

        forgot_passwordd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forgot_Password.class);
                startActivity(i);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Signup.class);
                startActivity(i);
            }
        });

    }

    private void loginbttn() {
        String email = l_email.getText().toString().trim();
        String password = l_pass.getText().toString().trim();

        if(email.isEmpty()){
            l_email.setError("Email is Required");
            l_email.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            l_email.setError("Please Enter a Valid Email!");
            l_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            l_pass.setError("Password is Required");
            l_email.requestFocus();
            return;
        }
        if(password.length() < 8){
            l_email.requestFocus();
            return;
        }
        pbBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,Splash_Screen.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your Email to Verify your Account!",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to Login! Please Check your Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}