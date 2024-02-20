package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText phoneNumberEditText;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        phoneNumberEditText = findViewById(R.id.enteredNumber);
        Button nextButton = findViewById(R.id.nextbtn1);
        countryCodePicker = findViewById(R.id.countryCode1);
        countryCodePicker.registerCarrierNumberEditText(phoneNumberEditText);

        // Set onClickListener for the "Next" button
        nextButton.setOnClickListener(view -> {
            // Check if the entered phone number is valid
            if (!countryCodePicker.isValidFullNumber()) {
                // Display an error message if the phone number is not valid
                phoneNumberEditText.setError("Valid Phone Number is Required");
            } else {
                // Retrieve the full phone number with the country code
                String phone = countryCodePicker.getFullNumberWithPlus();

                // Proceed to OTPActivity with the phone number as an extra
                navigateToOTPActivity(phone);
            }
        });
    }

    // Method to navigate to OTPActivity with the provided phone number
    private void navigateToOTPActivity(String phone) {
        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }
}
