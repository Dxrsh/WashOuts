package com.example.washouts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    TextView name,phone,feedbackView,helpView,outView;
    UserModel userModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = view.findViewById(R.id.fullname);
        phone = view.findViewById(R.id.number);
        getData();


        feedbackView = view.findViewById(R.id.feedbackview);
        helpView = view.findViewById(R.id.helpview);
        outView = view.findViewById(R.id.logoutview);


        feedbackView.setOnClickListener(v -> {
            if (HomeActivity.isAdmin) {
                Intent intent = new Intent(getActivity(), AllFeedbackActivity.class);
                getActivity().startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                getActivity().startActivity(intent);
            }
        });
        helpView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            getActivity().startActivity(intent);
        });
        outView.setOnClickListener(v -> {
            fAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
        });
        return view;
    }

    public void getData(){
        FireBase.getCurrentUserDetails().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    userModel = documentSnapshot.toObject(UserModel.class);
                    String fullName = null;
                    if (userModel != null) {
                        fullName = userModel.getFirstName()+" "+userModel.getLastName();
                    }
                    name.setText(fullName);
                    phone.setText(userModel.getPhoneNumber());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Data Retrieval Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}