package com.example.rest;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    EditText userEmail, password;
    Button login, continueGuest;
    TextView register, resetPass;
    ImageView logo;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent goToHomepage = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(goToHomepage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        continueGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Consider that you won't be able to book a table!", Toast.LENGTH_LONG).show();
                Intent goToHomepage = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(goToHomepage);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToResetPassword = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(goToResetPassword);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(email.isEmpty()){
                    userEmail.setError("Email address is required!");
                    userEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("Invalid format for email!");
                    userEmail.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                                Intent goToHomepage = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(goToHomepage);
                                finish();
                            }
                            else{
                                user.sendEmailVerification();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Check your email for verification, and then click again login!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            password.setText("");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Login Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Sign in with Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.googleSignIn);
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registerButton);
        continueGuest = findViewById(R.id.continueGuest);
        logo = findViewById(R.id.logo);
        resetPass = findViewById(R.id.resetPass);

        progressBar = findViewById(R.id.progressBar);

        Picasso.get().load(R.mipmap.logo_transparent).into(logo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                    Intent goToHomepage = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(goToHomepage);
                    finish();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Login Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}