package com.example.washouts;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    static String phoneNumber;
    static Boolean isAdmin;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.wash_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new WashFragment()).commit();

        // Set bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profile_menu) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ProfileFragment()).commit();
            } else if (item.getItemId() == R.id.order_menu) {
                if (isAdmin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new AllOrderFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new OrderFragment()).commit();
                }
            } else if (item.getItemId() == R.id.wash_menu) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new WashFragment()).commit();
            }
            return true;
        });


        // Get user phone number and check if user is an admin
        getPhoneNumber();
    }

    void getPhoneNumber() {
        FireBase.getCurrentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel != null) {
                    phoneNumber = userModel.getPhoneNumber();
                    isAdmin = checkIfUserIsAdmin();
                }
            }
        });
    }

    boolean checkIfUserIsAdmin() {
        String[] adminNumbers = getResources().getStringArray(R.array.admin);
        for (String adminNumber : adminNumbers) {
            if (adminNumber.equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }
}
