package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.AddressModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    String userId,add;
    EditText house,landmark;
    Button save;
    AddressModel addressModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.4));

        userId = FireBase.getCurrentUserId();
        house = findViewById(R.id.houseText);
        landmark = findViewById(R.id.landmarkText);
        save = findViewById(R.id.saveButton);
        add = getIntent().getStringExtra("address");

        save.setOnClickListener(v -> {
            setAddressOnBase();
        });
    }

    public void setAddressOnBase(){
        addressModel = new AddressModel(add,house.getText().toString(),landmark.getText().toString());
        Map<String,Object> address = new HashMap<>();
        address.put("address",addressModel);
        FireBase.getCurrentUserDetails().update(address)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddressActivity.this, "Address Stored", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddressActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddressActivity.this, "Address Storing Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}