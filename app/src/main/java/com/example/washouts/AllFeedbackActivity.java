package com.example.washouts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.OrderModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllFeedbackActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feedback);

        listView = findViewById(R.id.feedbackList);
        progressBar = findViewById(R.id.progressBar);

        getFeedbacks();
    }

    private void getFeedbacks() {
        FireBase.getFeedbacks().get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        List<Map<String,Object>> listData = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Map<String,Object> data = documentSnapshot.getData();
                            listData.add(data);
                        }
                        AdapterClass adapterClass = new AdapterClass(this,listData);
                        listView.setAdapter(adapterClass);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class AdapterClass extends ArrayAdapter<Map<String,Object>> {

        public AdapterClass(@NonNull Context context, List<Map<String,Object>> data) {
            super(context, R.layout.feedback_layout, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.feedback_layout,parent,false);
            }

            TextView feedback = convertView.findViewById(R.id.feedback);
            TextView name = convertView.findViewById(R.id.feedbackFrom);

            Map<String,Object> currentData = getItem(position);

            feedback.setText("\"" + currentData.get("feedback").toString() + "\"");
            name.setText("-" + currentData.get("firstName").toString() + " " + currentData.get("lastName").toString());

            return convertView;
        }
    }
}