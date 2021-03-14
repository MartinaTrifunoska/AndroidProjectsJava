package com.example.rest;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.rest.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, email, password, confirmPassword;
    Button registerButton;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fn = firstName.getText().toString().trim();
                final String ln = lastName.getText().toString().trim();
                final String em = email.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                String passC = confirmPassword.getText().toString().trim();

                if(fn.isEmpty()){
                    firstName.setError("First name is required!");
                    firstName.requestFocus();
                    return;
                }
                if(ln.isEmpty()){
                    lastName.setError("Last name is required!");
                    lastName.requestFocus();
                    return;
                }
                if(em.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
                    email.setError("Invalid format for email!");
                    email.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if(pass.length() < 6){
                    password.setError("Password needs to be at least 6 characters!");
                    password.requestFocus();
                    return;
                }
                if(passC.isEmpty()){
                    confirmPassword.setError("Confirmed password is required!");
                    confirmPassword.requestFocus();
                    return;
                }
                if(!pass.equals(passC)){
                    confirmPassword.setError("Password doesn't match!");
                    confirmPassword.setText("");
                    confirmPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fn,ln,em);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(moveToLogin);
                                        finish();
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this, "Failed to register, try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Failed to register, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);

        progressBar = findViewById(R.id.progressBar);
    }
}