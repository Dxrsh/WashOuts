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

public class ProfileFragment extends Fragment implements PaymentResultListener {

    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    TextView name,phone,feedbackView,helpView,outView;
    UserModel userModel;
    Button pay;
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
//        pay = view.findViewById(R.id.pay);
//
//        pay.setOnClickListener(v -> {
//            payNow("1");
//            Intent intent = new Intent(getActivity(),PaymentActivity.class);
//            getActivity().startActivity(intent);
//        });

        feedbackView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            getActivity().startActivity(intent);
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

    private void payNow(String amount) {
        final Activity activity = getActivity();
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_9V30Iow9nxYTdn");
        checkout.setImage(R.drawable.ic_launcher_background);

        double finalAmount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();
            options.put("name","Sunil");
            options.put("description","Reference No. #123");
            options.put("image","https://s3.amazonaws.com./rzp-mobile/images/rzp.png");
            options.put("theme.color",R.color.main);
            options.put("currency","INR");
            options.put("amount",finalAmount+"");
            options.put("prefill.contact","8828293618");

            checkout.open(activity,options);
        } catch (JSONException e) {
            Log.e("Tag","Error",e);
        }
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

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getActivity(), "Pay S", Toast.LENGTH_SHORT).show();
        pay.setText(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getActivity(), "Pay F", Toast.LENGTH_SHORT).show();
        pay.setText(s);
    }
}