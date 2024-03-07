package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

public class ConfirmCompleteOrderActivity extends AppCompatActivity {

    String orderId,userId;
    TextInputEditText orderIdEntered;
    Button confirmOrderComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_complete_order);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.2));

        orderIdEntered = findViewById(R.id.enterOrderId);
        confirmOrderComplete = findViewById(R.id.confirmOrderIdButton);

        orderId = getIntent().getExtras().getString("orderId");
        userId = getIntent().getExtras().getString("userId");

        confirmOrderComplete.setOnClickListener(v -> {
            if (orderIdEntered.getText().toString().equals(orderId.substring(0,5))) {
                FireBase.getAllUsers().document(userId).collection("orders")
                                .document(orderId).update("orderStatus","completed")
                        .addOnCompleteListener(task -> {
                            Toast.makeText(this, "Order Complete", Toast.LENGTH_SHORT).show();
                        });
                FireBase.getOrderDetails().whereEqualTo("orderId",orderId)
                        .get().addOnCompleteListener(task -> {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                FireBase.getOrderDetails().document(documentSnapshot.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            Toast.makeText(getApplicationContext(), "Order Removed", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
            } else {
                Toast.makeText(this, "Wrong Order ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}