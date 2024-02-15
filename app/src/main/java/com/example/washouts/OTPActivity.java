package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private String phoneNumber;
    private TextView phoneTextView;
    private TextInputEditText otpInput;
    private Button verifyBtn;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String verificationCode;
    private long timeoutSeconds = 60L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        // Initialize UI elements
        otpInput = findViewById(R.id.otpInput);
        verifyBtn = findViewById(R.id.verifyBtn);
        phoneTextView = findViewById(R.id.phone);

        // Get phone number from intent
        phoneNumber = getIntent().getExtras().getString("phone");
        phoneTextView.setText(phoneNumber);

        // Display phone number in a Toast (You might want to remove this for production)
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_LONG).show();

        // Send OTP to the provided phone number
        sendOtp(phoneNumber);

        // Set onClickListener for the "Verify" button
        verifyBtn.setOnClickListener(view -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
        });
    }

    // Method to send OTP to the provided phone number
    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                Toast.makeText(getApplicationContext(), "OTP sent successfully", Toast.LENGTH_LONG).show();
                            }
                        });
        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }

    // Method to sign in with the provided PhoneAuthCredential
    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        fAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "OTP Verification Successful", Toast.LENGTH_LONG).show();
                // Navigate to LoadActivity with the phone number as an extra
                navigateToLoadActivity(phoneNumber);
            } else {
                Toast.makeText(getApplicationContext(), "OTP Verification Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Method to navigate to LoadActivity with the provided phone number
    private void navigateToLoadActivity(String phone) {
        Intent intent = new Intent(OTPActivity.this, LoadActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
        finish();
    }
}
