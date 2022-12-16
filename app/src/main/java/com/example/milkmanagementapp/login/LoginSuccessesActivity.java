package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.customer.CustomerMainActivity;
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

public class LoginSuccessesActivity extends AppCompatActivity {

    private Button btnNext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String owner, customer, mobile_number;

    private void initialize() {
        btnNext = findViewById(R.id.btnNext);

        owner = "";
        customer = "";

        mobile_number = getIntent().getStringExtra("mobile_number");

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successed);

        initialize();

        Map<String, String> data = new HashMap<>();
        data.put("u", "");

        firebaseFirestore
                .collection("user")
                .document("user_manual")
                .set(data);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "+91" + mobile_number);
                firebaseFirestore
                        .collection("dairy_owner")
                        .whereEqualTo("mobile_number", "+91" + mobile_number)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value.size() != 0) {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("u", "owner");

                                    firebaseFirestore
                                            .collection("user")
                                            .document("user_manual")
                                            .set(data);

                                }
                            }
                        });

                firebaseFirestore
                        .collection("customer")
                        .whereEqualTo("mobile_number", "+91" + mobile_number)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                Log.d("tag", "Customer : " + String.valueOf(value.size()));
                                if (value.size() != 0) {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("u", "customer");

                                    firebaseFirestore
                                            .collection("user")
                                            .document("user_manual")
                                            .set(data);
                                }
                            }
                        });

                firebaseFirestore
                        .collection("user")
                        .document("user_manual")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String user = (String) documentSnapshot.get("u");
                                if (user.equals("customer")){
                                    startActivity(new Intent(LoginSuccessesActivity.this, CustomerMainActivity.class));
                                    finish();
                                }else if (user.equals("owner")){
                                    startActivity(new Intent(LoginSuccessesActivity.this, OwnerMainActivity.class));
                                    finish();
                                }else if (user.equals("")){
                                    startActivity(new Intent(LoginSuccessesActivity.this, SelectUserActivity.class));
                                    finish();
                                }
                            }
                        });


            }
        });
    }

}
