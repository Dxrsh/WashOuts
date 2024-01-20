package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button next;
    CountryCodePicker countryCodePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = findViewById(R.id.enteredNumber);
        next = findViewById(R.id.nextbtn1);
        countryCodePicker = findViewById(R.id.countryCode1);
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        next.setOnClickListener(view -> {
            if(!countryCodePicker.isValidFullNumber()){
                phoneNumber.setError("Valid Phone Number is Required");
            }else {
                String phone = countryCodePicker.getFullNumberWithPlus();
                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
    }
}