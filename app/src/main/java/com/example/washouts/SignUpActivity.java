package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private String phoneNum;
    private TextInputEditText fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        Button next = findViewById(R.id.nextbtn);

        // Get phone number from the intent
        phoneNum = Objects.requireNonNull(getIntent().getExtras()).getString("phone");

        // Set onClickListener for the "Next" button
        next.setOnClickListener(view -> {
            // Validate first name
            if (fName.getText().toString().isEmpty() || fName.getText().toString().length() < 3) {
                fName.setError("First Name should be more than 3 characters");
                return;
            }
            // Validate last name
            else if (lName.getText().toString().isEmpty() || lName.getText().toString().length() < 3) {
                lName.setError("Last Name should be more than 3 characters");
                return;
            }

            // Extract and trim first name and last name
            String firstName = fName.getText().toString().trim();
            String lastName = lName.getText().toString().trim();

            // Insert user data into Firebase
            insertData(firstName, lastName, phoneNum);
        });
    }

    // Method to insert user data into Firebase
    private void insertData(String firstName, String lastName, String phoneNum) {
        UserModel userModel = new UserModel(firstName, lastName, phoneNum);
        FireBase.getCurrentUserDetails().set(userModel)
                .addOnSuccessListener(unused -> {
                    // Data insertion successful, navigate to HomeActivity
                    Toast.makeText(SignUpActivity.this, "Inserted Data", Toast.LENGTH_SHORT).show();
                    navigateToHomeActivity();
                })
                .addOnFailureListener(e -> {
                    // Data insertion failed, show error message
                    Toast.makeText(SignUpActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to navigate to HomeActivity
    private void navigateToHomeActivity() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
