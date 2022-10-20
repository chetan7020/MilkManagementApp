package com.example.milkmanagementapp.owner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private View view;
    private TextView tvLogout, tvShareFeedback, tvName, tvID, tvDairyName, tvMobileNumber, tvAddress, tvTotalCustomer;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvLogout = view.findViewById(R.id.tvLogout);
        tvShareFeedback = view.findViewById(R.id.tvShareFeedback);

        tvDairyName = view.findViewById(R.id.tvDairyName);
        tvName = view.findViewById(R.id.tvName);
        tvID = view.findViewById(R.id.tvID);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        tvAddress = view.findViewById(R.id.tvAddress);

        tvTotalCustomer = view.findViewById(R.id.tvTotalCustomer);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_profile, container, false);

        initialize();

        setProfile();

        setTotalCustomer();

        tvShareFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Yet to Build", Toast.LENGTH_SHORT).show();
            }
        });


        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }

    private void setTotalCustomer() {
        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .whereNotEqualTo("count", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int total_customer = 0;
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            total_customer += Integer.parseInt(documentChange.getDocument().getData().get("count").toString());
                        }
                        tvTotalCustomer.setText(String.valueOf(total_customer));
                    }
                });
    }

    private void setProfile() {
        firebaseFirestore
                .collection("dairy_owner")
                .whereEqualTo("mobile_number", firebaseUser.getPhoneNumber())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String dairy_name = documentChange.getDocument().getData().get("dairy_name").toString();
                            String name = documentChange.getDocument().getData().get("name").toString();
                            String id = documentChange.getDocument().getData().get("id").toString();
                            String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();
                            String address = documentChange.getDocument().getData().get("address").toString();

                            tvDairyName.setText(dairy_name);
                            tvName.setText(name);
                            tvID.setText(id);
                            tvMobileNumber.setText(mobile_number);
                            tvAddress.setText(address);
                        }
                    }
                });
    }

}