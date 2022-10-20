package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.customer.CustomerMainActivity;
import com.example.milkmanagementapp.owner.OwnerMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SelectUserActivity extends AppCompatActivity {

    private Button btnNext;
    private RadioGroup rgUserType;
    private TextInputLayout etShopNameLayout;
    private RadioButton rbCustomer, rbDairyOwner;
    private TextInputEditText etDairyName, etName, etAddress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private void initialize() {
        btnNext = findViewById(R.id.btnNext);

        rgUserType = findViewById(R.id.rgUserType);

        etShopNameLayout = findViewById(R.id.etShopNameLayout);

        rbCustomer = findViewById(R.id.rbCustomer);
        rbDairyOwner = findViewById(R.id.rbDairyOwner);

        etDairyName = findViewById(R.id.etDairyName);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        initialize();

        rbCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShopNameLayout.setVisibility(View.GONE);
            }
        });

        rbDairyOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShopNameLayout.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dairy_name = etDairyName.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(address)) {

                    if (TextUtils.isEmpty(name)) {
                        etName.setError("Required");
                    }

                    if (TextUtils.isEmpty(address)) {
                        etAddress.setError("Required");
                    }

                } else {
                    etName.setError(null);
                    etAddress.setError(null);

                    Map<String, Object> data = new HashMap<>();
                    String id = String.valueOf(Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000)));

                    data.put("id", id);
                    data.put("name", name);
                    data.put("address", address);
                    data.put("mobile_number" , firebaseUser.getPhoneNumber());

                    if (rgUserType.getCheckedRadioButtonId() == rbCustomer.getId()) {

                        firebaseFirestore.collection("customer")
                                .document(firebaseUser.getPhoneNumber())
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(SelectUserActivity.this, CustomerMainActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else if (rgUserType.getCheckedRadioButtonId() == rbDairyOwner.getId()) {

                        if (TextUtils.isEmpty(dairy_name)) {
                            etDairyName.setError("Required");
                        } else {
                            etDairyName.setError(null);

                            data.put("dairy_name", dairy_name);

                            firebaseFirestore.collection("dairy_owner")
                                    .document(id)
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(SelectUserActivity.this, OwnerMainActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SelectUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }

                }

            }
        });
    }

}