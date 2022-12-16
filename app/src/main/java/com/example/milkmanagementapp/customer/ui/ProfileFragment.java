package com.example.milkmanagementapp.customer.ui;

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
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvName, tvID, tvMobileNumber, tvAddress, tvLogout;


    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvLogout = view.findViewById(R.id.tvLogout);

        tvName = view.findViewById(R.id.tvName);
        tvID = view.findViewById(R.id.tvID);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        tvAddress = view.findViewById(R.id.tvAddress);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_profile, container, false);

        initialize();

        firebaseFirestore.collection("customer")
                .whereEqualTo("mobile_number", firebaseUser.getPhoneNumber())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String name = documentChange.getDocument().getData().get("name").toString();
                            String id = documentChange.getDocument().getData().get("id").toString();
                            String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();
                            String address = documentChange.getDocument().getData().get("address").toString();

                            tvName.setText(name);
                            tvID.setText(id);
                            tvMobileNumber.setText(mobile_number);
                            tvAddress.setText(address);
                        }
                    }
                });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

}