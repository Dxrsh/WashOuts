package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    String phoneNum,userId;
    EditText fName,lName;
    Button next;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userId = FireBase.getCurrentUserId();
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        next=findViewById(R.id.nextbtn);

        phoneNum = getIntent().getExtras().getString("phone");

        next.setOnClickListener(view -> {
            if(fName.getText().toString().isEmpty() || fName.getText().toString().length()<3){
                fName.setError("First Name should be more than 3 char");
                return;
            }
            else if(lName.getText().toString().isEmpty() || lName.getText().toString().length()<3){
                lName.setError("Last Name should be more then 3 char");
                return;
            }

            String firstName = fName.getText().toString().trim();
            String lastName = lName.getText().toString().trim();

            insertData(firstName,lastName,phoneNum);
        });
    }

    private void insertData(String firstName, String lastName, String phoneNum) {
        userModel = new UserModel(firstName,lastName,phoneNum);
        FireBase.getCurrentUserDetails().set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignUpActivity.this, "Inserted Data", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}