package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a Handler to delay the execution and show the splash screen for 1500 milliseconds (1.5 seconds)
        checkNotificationPermission();
    }

    private void handlerCode() {
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

    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            handlerCode();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handlerCode();
            } else {
                Toast.makeText(this, "Please Allow Notification Permission", Toast.LENGTH_SHORT).show();
                // Check if the user clicked "Don't Allow" and redirect to app settings
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    redirectToAppSettings();
                } else {
                    // Keep asking for permission if the user clicks "Don't Allow"
                    checkNotificationPermission();
                }
            }
        }
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

    // Method to redirect the user to the app settings
    private void redirectToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
        checkNotificationPermission();
    }
}
