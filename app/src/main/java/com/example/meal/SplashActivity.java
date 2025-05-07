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
            startActivity(new Intent(SplashActivity.this, FirstTimeActivity.class));
            finish();
        }, 3000);

    }
}