package com.example.washouts.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBase {
    public static String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static DocumentReference getCurrentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(getCurrentUserId());
    }

    public static CollectionReference getOrderDetails() {
        return FirebaseFirestore.getInstance().collection("orders");
    }

    public static CollectionReference getUsersOrders() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(getCurrentUserId()).collection("orders");
    }
}
