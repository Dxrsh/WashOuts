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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllFeedbackActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feedback);

        // Initialize views
        listView = findViewById(R.id.feedbackList);
        progressBar = findViewById(R.id.progressBar);

        // Fetch and display feedbacks
        getFeedbacks();
    }

    private void getFeedbacks() {
        FireBase.getFeedbacks().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Hide the progress bar
                progressBar.setVisibility(View.GONE);

                // Process feedback data and set up the adapter
                List<Map<String, Object>> listData = processFeedbackData(task);
                setupListAdapter(listData);
            } else {
                // Display an error message if there is an issue with fetching data
                Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Map<String, Object>> processFeedbackData(@NonNull Task<QuerySnapshot> task) {
        List<Map<String, Object>> listData = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : task.getResult()) {
            Map<String, Object> data = documentSnapshot.getData();
            listData.add(data);
        }
        return listData;
    }

    private void setupListAdapter(List<Map<String, Object>> listData) {
        // Create and set up the custom adapter
        AdapterClass adapterClass = new AdapterClass(this, listData);
        listView.setAdapter(adapterClass);
    }

    public static class AdapterClass extends ArrayAdapter<Map<String, Object>> {

        public AdapterClass(@NonNull Context context, List<Map<String, Object>> data) {
            super(context, R.layout.feedback_layout, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Inflate the layout if the view is not yet created
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.feedback_layout, parent, false);
            }

            // Retrieve views from the layout
            TextView feedback = convertView.findViewById(R.id.feedback);
            TextView name = convertView.findViewById(R.id.feedbackFrom);

            // Get current feedback data
            Map<String, Object> currentData = getItem(position);

            // Set text for feedback and name views
            if (currentData != null) {
                feedback.setText("\"" + currentData.get("feedback") + "\"");
                name.setText("-" + currentData.get("firstName") + " " + currentData.get("lastName"));
            }

            return convertView;
        }
    }
}
