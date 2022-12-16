package com.example.milkmanagementapp.customer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private View view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private LinearLayout llData;
    private SearchView svSearch;

    private void initialize() {
        llData = view.findViewById(R.id.llData);

        svSearch = view.findViewById(R.id.svSearch);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment_home, container, false);

        initialize();

        getData();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                for (int i = 0; i < llData.getChildCount(); i++) {

                    TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);
                    TextView tvName = llData.getChildAt(i).findViewById(R.id.tvName);
                    CardView cvCustomerCard = llData.getChildAt(i).findViewById(R.id.cvCustomerCard);

                    if (!(tvName.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvID.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        cvCustomerCard.setVisibility(View.GONE);
                    } else {
                        cvCustomerCard.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }
        });


        return view;
    }

    private void getData() {
        firebaseFirestore
                .collection("dairy_owner")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String id = documentChange.getDocument().getData().get("id").toString();
                            String dairy_name = documentChange.getDocument().getData().get("dairy_name").toString();
                            String address = documentChange.getDocument().getData().get("address").toString();
                            String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();

                            addDairy(id, dairy_name, address, mobile_number);
                        }
                    }
                });

    }

    private void addDairy(String id, String dairy_name, String address, String mobile_number) {
        View view = getLayoutInflater().inflate(R.layout.customer_dairy_card_layout, null, false);

        TextView tvID = view.findViewById(R.id.tvID);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvAddress = view.findViewById(R.id.tvAdd);
        TextView tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);

        Button btnAddDairy = view.findViewById(R.id.btnAddDairy);

        btnAddDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();

                data.put("id", tvID.getText().toString());
                data.put("dairy_name", tvName.getText().toString());
                data.put("address", tvAddress.getText().toString());
                data.put("mobile_number", tvPhoneNumber.getText().toString());

                firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_customer")
                        .document(tvID.getText().toString())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Dairy Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        tvID.setText(id);
        tvName.setText(dairy_name);
        tvAddress.setText(address);
        tvPhoneNumber.setText(mobile_number);

        llData.addView(view);
    }

}