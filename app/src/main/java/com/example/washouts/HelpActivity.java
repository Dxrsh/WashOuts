package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.washouts.help.OrderHelpActivity;
import com.example.washouts.help.OrderListHelpActivity;
import com.example.washouts.help.RefundHelpActivity;

public class HelpActivity extends AppCompatActivity {

    LinearLayout orderHelp,orderListHelp,refundHelp;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        orderHelp = findViewById(R.id.orderHelp);
        orderListHelp = findViewById(R.id.orderListHelp);
        refundHelp = findViewById(R.id.refundHelp);

        orderHelp.setOnClickListener(v -> {
            intent = new Intent(this, OrderHelpActivity.class);
            startActivity(intent);
        });
        orderListHelp.setOnClickListener(v -> {
            intent = new Intent(this, OrderListHelpActivity.class);
            startActivity(intent);
        });
        refundHelp.setOnClickListener(v -> {
            intent = new Intent(this, RefundHelpActivity.class);
            startActivity(intent);
        });

    }
}