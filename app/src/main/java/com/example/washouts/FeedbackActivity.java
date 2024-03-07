package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private TextInputEditText feedbackText;
    private Button send;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Set the dimensions of the activity window
        setWindowDimensions();

        // Initialize views
        feedbackText = findViewById(R.id.feedback);
        send = findViewById(R.id.feedback_button);

        // Fetch current user details
        fetchCurrentUserDetails();

        // Set click listener for the send button
        setSendButtonClickListener();
    }

    private void setWindowDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
    }

    private void fetchCurrentUserDetails() {
        FireBase.getCurrentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                userModel = documentSnapshot.toObject(UserModel.class);
            }
        });
    }

    private void setSendButtonClickListener() {
        send.setOnClickListener(v -> {
            // Validate and send feedback
            validateAndSendFeedback();
        });
    }

    private void validateAndSendFeedback() {
        if (feedbackText.getText().toString().isEmpty()) {
            feedbackText.setError("Give proper feedback");
        } else {
            String feedback = feedbackText.getText().toString();

            // Create a map to store feedback details
            Map<String, String> feedbackMap = new HashMap<>();
            feedbackMap.put("firstName", userModel.getFirstName());
            feedbackMap.put("lastName", userModel.getLastName());
            feedbackMap.put("feedback", feedback);

            // Add feedback to the Firestore database
            FireBase.getFeedbacks().add(feedbackMap).addOnSuccessListener(documentReference -> {
                // Display success message and finish the activity
                displayFeedbackSentMessage();
                finish();
            });
        }
    }

    private void displayFeedbackSentMessage() {
        Toast.makeText(getApplicationContext(), "Feedback sent", Toast.LENGTH_LONG).show();
    }
}
