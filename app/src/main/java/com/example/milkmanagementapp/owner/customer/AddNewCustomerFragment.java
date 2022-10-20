package com.example.milkmanagementapp.owner.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewCustomerFragment extends Fragment {

    private View view;
    private TextInputEditText etName, etMobileNumber;
    private Button btnSave;
    private RadioGroup rgSellerBuyer, rgActiveInActive;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private void initialize() {
        etName = view.findViewById(R.id.etName);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);

        rgSellerBuyer = view.findViewById(R.id.rgSellerBuyer);
        rgActiveInActive = view.findViewById(R.id.rgActiveInActive);

        btnSave = view.findViewById(R.id.btnSave);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_customer_fragment_add_new_customer, container, false);

        initialize();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString().trim();
                String mobile_number = etMobileNumber.getText().toString().trim();
                String type = "", status = "";
                if (rgSellerBuyer.getCheckedRadioButtonId() == R.id.rbSeller) {
                    type = "Seller";
                } else if (rgSellerBuyer.getCheckedRadioButtonId() == R.id.rbBuyer) {
                    type = "Buyer";
                }

                if (rgActiveInActive.getCheckedRadioButtonId() == R.id.rbActive) {
                    status = "Active";
                } else if (rgActiveInActive.getCheckedRadioButtonId() == R.id.rbInActive) {
                    status = "InActive";
                }

                if (name.equals("") || mobile_number.equals("")) {

                    if (name.equals("")) {
                        etName.setError("Required");
                    }

                    if (mobile_number.equals("")) {
                        etMobileNumber.setError("Required");
                    }

                } else {

                    etName.setError(null);
                    etMobileNumber.setError(null);

                    Map<Object, String> data = new HashMap<>();
                    String id = String.valueOf(Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000)));

                    data.put("id", id);
                    data.put("name", name);
                    data.put("mobile_number", mobile_number);
                    data.put("type", type);
                    data.put("status", status);

                    if (firebaseUser != null) {
                        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_customer")
                                .document(id)
                                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        etName.setText("");
                                        etMobileNumber.setText("");
                                        Log.d("tag", "value inserted");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("tag", e.getMessage());
                                    }
                                });
                    } else {
                        Log.d("tag", "Failed to Add Customer");
                    }

                }

            }
        });

        return view;
    }

}