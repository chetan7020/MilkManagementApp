package com.example.milkmanagementapp.owner.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.owner.customer.AddNewCustomerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerFragment extends Fragment {

    private View view;
    private Button btnAddNew;
    private LinearLayout llView;
    private FrameLayout frameLayout;
    private LinearLayout llData;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvNoDataFound, tvActive, tvInActive;
    private SearchView svSearch;

    private void initialize() {
        btnAddNew = view.findViewById(R.id.btnAddNew);

        frameLayout = view.findViewById(R.id.frame_layout);

        llData = view.findViewById(R.id.llData);

        llView = view.findViewById(R.id.llView);

        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
        tvActive = view.findViewById(R.id.tvActive);
        tvInActive = view.findViewById(R.id.tvInActive);

        svSearch = view.findViewById(R.id.svSearch);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d("tag" , "initialize complete");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_customer, container, false);
        Log.d("tag" , "on complete start");

        initialize();

        setCustomerCount();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                for (int i = 0; i < llData.getChildCount(); i++) {

                    TextView tvName = llData.getChildAt(i).findViewById(R.id.tvName);
                    TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);
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

        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .whereNotEqualTo("id", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.size() == 0) {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                String id = documentChange.getDocument().getData().get("id").toString();
                                String name = documentChange.getDocument().getData().get("name").toString();
                                String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();
                                String type = documentChange.getDocument().getData().get("type").toString();
                                String status = documentChange.getDocument().getData().get("status").toString();

                                addCustomer(id, name, mobile_number, type, status);
                            }
                        }
                    }
                });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                llView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_layout, new AddNewCustomerFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        return view;
    }

    private void setCustomerCount() {
        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .document("active")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvActive.setText("Active : " + documentSnapshot.getString("count"));
                    }
                });

        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .document("inactive")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvInActive.setText("In-Active : " + documentSnapshot.getString("count"));
                    }
                });
    }

    private void addCustomer(String id, String name, String mobile_number, String type, String status) {
        View view = getLayoutInflater().inflate(R.layout.owner_customer_card_layout, null, false);

        TextView tvID = view.findViewById(R.id.tvID);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        TextView tvCustomerType = view.findViewById(R.id.tvCustomerType);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        tvID.setText(id);
        tvName.setText(name);
        tvMobileNumber.setText(mobile_number);
        tvCustomerType.setText(type);
        tvStatus.setText(status);

        llData.addView(view);
    }
}