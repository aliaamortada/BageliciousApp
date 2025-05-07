package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstTimeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialButton btnLogin;
    private TextView laterTextView;
    private TextView createNewAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        btnLogin = findViewById(R.id.btnLogin);
        laterTextView = findViewById(R.id.later);
        createNewAccountTextView = findViewById(R.id.createNewAccount);

        mAuth = FirebaseAuth.getInstance();

        // When the "Later" button is clicked, sign in anonymously
        laterTextView.setOnClickListener(v -> {
                            FirebaseUser guestUser = mAuth.getCurrentUser();
                            Toast.makeText(this, "Signed in as Guest", Toast.LENGTH_SHORT).show();

                            // Navigate to main/home screen
                            Intent homeIntent = new Intent(FirstTimeActivity.this, MainActivity.class); // Or HomeActivity
                            startActivity(homeIntent);
                            finish();
        });

        // When the "Login" button is clicked, go to LoginActivity
        btnLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(FirstTimeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        // When the "Create New Account" button is clicked, go to SignupActivity
        createNewAccountTextView.setOnClickListener(v -> {
            Intent signupIntent = new Intent(FirstTimeActivity.this, SignUpActivity.class);
            startActivity(signupIntent);
            finish();
        });
    }


}
