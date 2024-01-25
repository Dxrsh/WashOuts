package com.example.washouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    EditText feedbackText;
    String feedback;
    Button send;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        feedbackText = findViewById(R.id.feedback);
        send = findViewById(R.id.feedback_button);

        FireBase.getCurrentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                userModel = documentSnapshot.toObject(UserModel.class);
            }
        });

        send.setOnClickListener(v -> {
            if(feedbackText.getText().toString().isEmpty()){
                feedbackText.setError("Give proper feedback");
            }else{
                feedback = feedbackText.getText().toString();
                Map<String,String> fdk = new HashMap<>();
                fdk.put("firstName",userModel.getFirstName());
                fdk.put("lastName",userModel.getLastName());
                fdk.put("feedback",feedback);
                FireBase.getFeedbacks()
                        .add(fdk)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Feedback sent",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}