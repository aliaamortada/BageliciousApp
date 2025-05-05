package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class FirstTimeActivity extends AppCompatActivity {

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


        // When the "Later" button is clicked, go to HomeFragment
        laterTextView.setOnClickListener(v -> {
           // navigateToHomeFragment();
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
//
//    // Method to navigate to HomeFragment
//    private void navigateToHomeFragment() {
//        // Check if the fragment is already in the fragment manager
//        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
//
//        if (homeFragment == null) {
//            homeFragment = new HomeFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.main, homeFragment, HomeFragment.class.getSimpleName());
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//    }

}
