package com.example.washouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileFragment extends Fragment {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private TextView name, phone, feedbackView, helpView, outView;
    private UserModel userModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        name = view.findViewById(R.id.fullname);
        phone = view.findViewById(R.id.number);
        feedbackView = view.findViewById(R.id.feedbackview);
        helpView = view.findViewById(R.id.helpview);
        outView = view.findViewById(R.id.logoutview);

        // Fetch user data
        getData();

        // Configure UI based on user role
        if (HomeActivity.isAdmin) {
            feedbackView.setText("View Feedbacks");
        }

        // Set click listeners for UI elements
        setClickListeners();

        return view;
    }

    private void setClickListeners() {
        feedbackView.setOnClickListener(v -> {
            // Open appropriate feedback activity based on user role
            if (HomeActivity.isAdmin) {
                startActivity(new Intent(getActivity(), AllFeedbackActivity.class));
            } else {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            }
        });

        helpView.setOnClickListener(v -> startActivity(new Intent(getActivity(), HelpActivity.class)));

        outView.setOnClickListener(v -> {
            // Sign out user and unsubscribe from admin topic
            signOutAndUnsubscribe();
        });
    }

    private void signOutAndUnsubscribe() {
        fAuth.signOut();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void getData() {
        FireBase.getCurrentUserDetails().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel != null) {
                    // Display user data
                    displayUserData(userModel);
                }
            }
        }).addOnFailureListener(e -> {
            // Handle data retrieval failure
            handleDataRetrievalFailure();
        });
    }

    private void displayUserData(UserModel userModel) {
        String fullName = userModel.getFirstName() + " " + userModel.getLastName();
        name.setText(fullName);
        phone.setText(userModel.getPhoneNumber());
    }

    private void handleDataRetrievalFailure() {
        Toast.makeText(getActivity(), "Data Retrieval Failed", Toast.LENGTH_SHORT).show();
    }
}
