package com.example.meal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView; // **ADDED**: Import for BottomNavigationView
import com.example.meal.AccountFragment;
import com.example.meal.CalenderFragment;
import com.example.meal.FavouriteFragment;
import com.example.meal.HomeFragment;
import com.example.meal.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // **ADDED**: Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

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


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

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

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    // Helper method to load fragments
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}