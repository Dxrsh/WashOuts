package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class LoadActivity extends AppCompatActivity {

    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // Get current user phone number from the intent
        phoneNum = Objects.requireNonNull(getIntent().getExtras()).getString("phone");

        // Retrieve user data and proceed accordingly
        getData();
    }

    private void getData() {
        FireBase.getCurrentUserDetails().get()
                .addOnSuccessListener(this::handleDataSuccess)
                .addOnFailureListener(this::handleDataFailure);
    }

    private void handleDataSuccess(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            // User data exists, navigate to HomeActivity
            UserModel userModel = documentSnapshot.toObject(UserModel.class);
            if (userModel != null) {
                Toast.makeText(LoadActivity.this, "Welcome " + userModel.getFirstName(),
                        Toast.LENGTH_SHORT).show();
                navigateToHomeActivity();
            }
        } else {
            // User data doesn't exist, navigate to SignUpActivity
            navigateToSignUpActivity();
        }
    }

    private void handleDataFailure(@NonNull Exception e) {
        Toast.makeText(LoadActivity.this, "Data Retrieval Failed", Toast.LENGTH_SHORT).show();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(LoadActivity.this, HomeActivity.class);
        intent.putExtra("phone", phoneNum);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(LoadActivity.this, SignUpActivity.class);
        intent.putExtra("phone", phoneNum);
        startActivity(intent);
        finish();
    }
}
