package com.example.washouts;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.OrderModel;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckOutActivity extends AppCompatActivity implements PaymentResultListener {

    OrderModel orderModel;
    String userId,orderId,fullName,address,date,time,service,garments,mOPayment="",payment="";
    TextView addressTV,dateTV,timeTV,serviceTV,garmentsTV,amountTV;
    RadioGroup modeOfPayment;
    Button confirm;
    UserModel userModel;
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
        amountTV = findViewById(R.id.amount);

        FireBase.getCurrentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                userModel = documentSnapshot.toObject(UserModel.class);
            }
        });

        addressTV.setText(address);
        dateTV.setText(date);
        timeTV.setText(time);
        serviceTV.setText(service);
        garmentsTV.setText(garments);
        calculatePayment();


        modeOfPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                switch (radioButton.getText().toString()) {
                    case "Cash On Delivery":
                        mOPayment = "cash";
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
                    case "cash":
                        placeOrder();
                        break;
                    case "online":
                        payOnline(payment);
                        break;
                }
            }
        });
    }

    private void payOnline(String payment) {
        final Activity activity = this;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_9V30Iow9nxYTdn");
        checkout.setImage(R.drawable.ic_launcher_background);

        double finalAmount = Float.parseFloat(payment)*100;

        try {
            JSONObject options = new JSONObject();
            options.put("name",fullName);
            options.put("image","https://s3.amazonaws.com./rzp-mobile/images/rzp.png");
            options.put("theme.color",R.color.main);
            options.put("currency","INR");
            options.put("amount",finalAmount+"");
            options.put("prefill.contact",userModel.getPhoneNumber());

            checkout.open(activity,options);
        } catch (JSONException e) {
            Log.e("Tag","Error",e);
        }
    }

    private void calculatePayment() {
        Float amount;
        switch (service) {
            case "Steam Press Only":
                amount = Float.parseFloat(garments) * 30;
                payment = String.valueOf(amount);
                amountTV.setText("Rs." + payment);
                break;
            case "Dry Cleaning & Steam Press":
                amount = Float.parseFloat(garments) * 50;
                payment = String.valueOf(amount);
                amountTV.setText("Rs." + payment);
                break;
            case "Wash Cleaning & Steam Press":
                amount = Float.parseFloat(garments) * 80;
                payment = String.valueOf(amount);
                amountTV.setText("Rs." + payment);
                break;
        }
    }

    public void placeOrder() {
        orderModel = new OrderModel(userId,"",fullName,address,date,time,service,garments,payment,mOPayment,"Active");
        FireBase.getUsersOrders().add(orderModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = task.getResult();
                    orderId = documentReference.getId();
                    orderModel.setOrderId(orderId);
                    FireBase.getUsersOrders().document(orderId).update("orderId",orderId).addOnCompleteListener(task1 -> {
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


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
        placeOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}