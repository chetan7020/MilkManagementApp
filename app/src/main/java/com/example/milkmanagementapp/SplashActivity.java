package com.example.milkmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.customer.CustomerMainActivity;
import com.example.milkmanagementapp.login.LoginActivity;
import com.example.milkmanagementapp.owner.OwnerMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Map<String, String> data = new HashMap<>();
        data.put("u", "");

        firebaseFirestore
                .collection("user")
                .document("user_auto")
                .set(data);

        if (firebaseUser != null) {
            firebaseFirestore
                    .collection("dairy_owner")
                    .whereEqualTo("mobile_number", firebaseUser.getPhoneNumber())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            Log.d("tag", "Owner : " + String.valueOf(value.size()));
                            if (value.size() != 0) {
                                Map<String, String> data = new HashMap<>();
                                data.put("u", "owner");

                                firebaseFirestore
                                        .collection("user")
                                        .document("user_auto")
                                        .set(data);

                            }
                        }
                    });

            firebaseFirestore
                    .collection("customer")
                    .whereEqualTo("mobile_number", firebaseUser.getPhoneNumber())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            Log.d("tag", "Customer : " + String.valueOf(value.size()));
                            if (value.size() != 0) {
                                Map<String, String> data = new HashMap<>();
                                data.put("u", "customer");

                                firebaseFirestore
                                        .collection("user")
                                        .document("user_auto")
                                        .set(data);
                            }
                        }
                    });
        }


        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (firebaseUser != null) {
                    firebaseFirestore
                            .collection("user")
                            .document("user_auto")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String user = (String) documentSnapshot.get("u");
                                    if (user.equals("customer")) {
                                        startActivity(new Intent(SplashActivity.this, CustomerMainActivity.class));
                                        finish();
                                    } else if (user.equals("owner")) {
                                        startActivity(new Intent(SplashActivity.this, OwnerMainActivity.class));
                                        finish();
                                    } else if (user.equals("")) {
                                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1000);

    }
}