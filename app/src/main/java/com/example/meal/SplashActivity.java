package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashGif = findViewById(R.id.splashGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.splashvid)
                .into(splashGif);

        // Delay to move to main activity
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class)); // Logged in
            } else {
                startActivity(new Intent(SplashActivity.this, FirstTimeActivity.class)); //Not logged in
            }
            finish();
        }, 3000);

    }
}