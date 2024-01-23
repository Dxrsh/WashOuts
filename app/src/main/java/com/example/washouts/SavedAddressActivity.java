package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.AddressModel;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class SavedAddressActivity extends AppCompatActivity {

    TextView houseView,landmarkView,locationView,changeView;
    String house,landmark,location;
    Button ok;
    UserModel userModel;
    AddressModel addressModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.4));

        houseView = findViewById(R.id.houseText_saved);
        landmarkView = findViewById(R.id.landmarkText_saved);
        locationView = findViewById(R.id.gpsText_saved);
        changeView = findViewById(R.id.changeAddress);
        ok = findViewById(R.id.okButton);

        getAddressFromBase();

        changeView.setOnClickListener(v -> {
            Intent intent = new Intent(SavedAddressActivity.this, MapActivity.class);
            startActivity(intent);
        });
        ok.setOnClickListener(v -> {
            Intent intent = new Intent(SavedAddressActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    public void getAddressFromBase(){
        FireBase.getCurrentUserDetails().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    userModel = documentSnapshot.toObject(UserModel.class);
                    if(userModel!=null){
                        addressModel = userModel.getAddress();

                        house = addressModel.getHouse();
                        landmark = addressModel.getLandmark();
                        location = addressModel.getLocation();

                        houseView.setText(house);
                        landmarkView.setText(landmark);
                        locationView.setText(location);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SavedAddressActivity.this, "Get Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}