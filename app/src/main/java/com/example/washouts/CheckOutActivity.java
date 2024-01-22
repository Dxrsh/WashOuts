package com.example.washouts;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class CheckOutActivity extends AppCompatActivity {

    OrderModel orderModel;
    String userId,orderId,fullName,address,date,time,service,garments,mOPayment="";
    TextView addressTV,dateTV,timeTV,serviceTV,garmentsTV;
    RadioGroup modeOfPayment;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        userId = FireBase.getCurrentUserId();
        fullName = getIntent().getExtras().getString("fullname");
        address = getIntent().getExtras().getString("address");
        date = getIntent().getExtras().getString("date");
        time = getIntent().getExtras().getString("time");
        service = getIntent().getExtras().getString("service");
        garments = getIntent().getExtras().getString("garments");

        addressTV = findViewById(R.id.displayAddTV);
        dateTV = findViewById(R.id.displayDateTV);
        timeTV = findViewById(R.id.displayTimeTV);
        serviceTV = findViewById(R.id.displayServiceTV);
        garmentsTV = findViewById(R.id.displaygarmentsTV);
        modeOfPayment = findViewById(R.id.modeOfPayment);
        confirm = findViewById(R.id.confirmOrderButton);

        addressTV.setText(address);
        dateTV.setText(date);
        timeTV.setText(time);
        serviceTV.setText(service);
        garmentsTV.setText(garments);

        modeOfPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                switch (radioButton.getText().toString()) {
                    case "Cash On Delivery":
                        mOPayment = "cod";
                        break;
                    case "Online/UPI":
                        mOPayment = "online";
                        break;
                }
            }
        });

        confirm.setOnClickListener(v -> {
            if(mOPayment.isEmpty()) {
                Toast.makeText(this, "Please select mode of payment!", Toast.LENGTH_SHORT).show();
            } else {
                switch (mOPayment) {
                    case "cod":
                        placeOrder();
                        break;
                    case "online":
                        placeOrder();
                        break;
                }
            }
        });
    }

    public void placeOrder() {
        orderModel = new OrderModel(userId,"",fullName,address,date,time,service,garments,"100",mOPayment);
        FireBase.getUsersOrders().add(orderModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = task.getResult();
                    orderId = documentReference.getId();
                    orderModel.setOrderId(orderId);
                    FireBase.getOrderDetails().document(orderId).update("orderId",orderId).addOnCompleteListener(task1 -> {
                        Toast.makeText(CheckOutActivity.this, "Order Placed On User", Toast.LENGTH_SHORT).show();
                        FireBase.getOrderDetails().add(orderModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CheckOutActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    });
                }
            }
        });

    }
}