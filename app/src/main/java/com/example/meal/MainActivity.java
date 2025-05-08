package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Import for BottomNavigationView
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for internet at startup
        boolean isOnline = isInternetAvailable();
        if (!isOnline) {
            android.widget.Toast.makeText(MainActivity.this, "Offline mode", android.widget.Toast.LENGTH_SHORT).show();
            loadFragment(new FavouriteFragment());
            return; // Exit onCreate to avoid setting up navigation
        }

        // Set up Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            boolean isLoggedIn = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null;

            // Check for internet connection
            boolean isOnlineInner = isInternetAvailable();
            if (!isOnlineInner && (id == R.id.home || id == R.id.search)) {
                android.widget.Toast.makeText(MainActivity.this, "You're offline, you need internet connection", android.widget.Toast.LENGTH_SHORT).show();
                return false; // Prevent navigation
            }

            if (!isOnlineInner) {
                android.widget.Toast.makeText(MainActivity.this, "Offline mode", android.widget.Toast.LENGTH_SHORT).show();
                selectedFragment = new FavouriteFragment(); //ADDED
            } else if ((id == R.id.calender || id == R.id.favourite || id == R.id.account) && !isLoggedIn) {
                startActivity(new Intent(MainActivity.this, FirstTimeActivity.class));
                android.widget.Toast.makeText(MainActivity.this, "You must login first", android.widget.Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (id == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (id == R.id.search) {
                    selectedFragment = new SearchFragment();
                } else if (id == R.id.calender) {
                    selectedFragment = new CalenderFragment();
                } else if (id == R.id.favourite) {
                    selectedFragment = new FavouriteFragment();
                } else if (id == R.id.account) {
                    selectedFragment = new AccountFragment();
                }
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Set initial fragment if there is no saved state
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }
    //  Internet availability checker
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnected();
    }
    // Helper method to load fragments
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}