package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    String phoneNumber;
    TextView phone;
    EditText otpInput;
    Button verifyBtn;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String verificationCode;
    Long timeoutSeconds = 60L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        otpInput = findViewById(R.id.otpInput);
        verifyBtn = findViewById(R.id.verifyBtn);
        phone = findViewById(R.id.phone);

        phoneNumber=getIntent().getExtras().getString("phone");
        phone.setText(phoneNumber);
        Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_LONG).show();


        sendOtp(phoneNumber);

        verifyBtn.setOnClickListener(view -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            signIn(credential);
        });
    }

    void sendOtp(String phoneNumber){
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
                                Toast.makeText(getApplicationContext(),"Verification Failed",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_LONG).show();
                            }
                        });
        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        fAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"OTP Verification Successful",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OTPActivity.this, LoadActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"OTP Verification Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}