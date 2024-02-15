package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.washouts.firebase.FireBase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a Handler to delay the execution and show the splash screen for 1500 milliseconds (1.5 seconds)
        new Handler().postDelayed(() -> {
            // Check if a user is already logged in using Firebase authentication
            if (FireBase.getCurrentUser() != null) {
                // User is logged in, redirect to HomeActivity
                navigateToHome();
            } else {
                // User is not logged in, redirect to LoginActivity
                navigateToLogin();
            }
            finish(); // Finish the current activity after navigation
        }, 1500);
    }

    // Method to navigate to the HomeActivity
    private void navigateToHome() {
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }

    // Method to navigate to the LoginActivity
    private void navigateToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}