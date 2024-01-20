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

public class LoadActivity extends AppCompatActivity {

    String phoneNum,userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        userId = FireBase.getCurrentUserId();
        phoneNum = getIntent().getExtras().getString("phone");

        getData();
    }

    public void getData(){
        FireBase.getCurrentUserDetails().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel!=null) {
                        Toast.makeText(LoadActivity.this, "Welcome "+userModel.getFirstName(),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoadActivity.this, HomeActivity.class);
                        intent.putExtra("phone",phoneNum);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(LoadActivity.this, SignUpActivity.class);
                    intent.putExtra("phone",phoneNum);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoadActivity.this, "Data Retrieval Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}