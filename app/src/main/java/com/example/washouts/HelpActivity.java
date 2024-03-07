package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.washouts.help.OrderHelpActivity;
import com.example.washouts.help.OrderListHelpActivity;
import com.example.washouts.help.RefundHelpActivity;

public class HelpActivity extends AppCompatActivity {

    private LinearLayout orderHelp, orderListHelp, refundHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Initialize views
        orderHelp = findViewById(R.id.orderHelp);
        orderListHelp = findViewById(R.id.orderListHelp);
        refundHelp = findViewById(R.id.refundHelp);

        // Set click listeners for help categories
        setClickListeners();
    }

    private void setClickListeners() {
        orderHelp.setOnClickListener(v -> navigateToActivity(OrderHelpActivity.class));
        orderListHelp.setOnClickListener(v -> navigateToActivity(OrderListHelpActivity.class));
        refundHelp.setOnClickListener(v -> navigateToActivity(RefundHelpActivity.class));
    }

    private void navigateToActivity(Class<?> destinationActivity) {
        // Navigate to the specified help category activity
        Intent intent = new Intent(this, destinationActivity);
        startActivity(intent);
    }
}
