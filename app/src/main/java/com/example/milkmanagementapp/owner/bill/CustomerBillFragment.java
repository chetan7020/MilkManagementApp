package com.example.milkmanagementapp.owner.bill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerBillFragment extends Fragment {

    private View view;
    private AutoCompleteTextView spCustomerList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvSBTotalPayableAmount, tvSBPaidAmount, tvSBRemainingAmount, tvSBCustomerName;
    private TextView tvBBTotalReceivableAmount, tvBBReceivedAmount, tvBBRemainingAmount, tvBBCustomerName;
    private Button btnSearch;

    private void initialize() {
        spCustomerList = view.findViewById(R.id.spCustomerList);

        tvSBTotalPayableAmount = view.findViewById(R.id.tvSBTotalPayableAmount);
        tvSBPaidAmount = view.findViewById(R.id.tvSBPaidAmount);
        tvSBRemainingAmount = view.findViewById(R.id.tvSBRemainingAmount);
        tvSBCustomerName = view.findViewById(R.id.tvSBCustomerName);

        tvBBTotalReceivableAmount = view.findViewById(R.id.tvBBTotalReceivableAmount);
        tvBBReceivedAmount = view.findViewById(R.id.tvBBReceivedAmount);
        tvBBRemainingAmount = view.findViewById(R.id.tvBBRemainingAmount);
        tvBBCustomerName = view.findViewById(R.id.tvBBCustomerName);

        btnSearch = view.findViewById(R.id.btnSearch);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_bill_fragment_customer_bill, container, false);

        initialize();

        setDropDownList();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected = spCustomerList.getEditableText().toString();

                if (selected.equals("Select Customer")) {
                    Toast.makeText(getActivity(), "Select Customer First", Toast.LENGTH_SHORT).show();
                } else {
                    tvSBCustomerName.setText("Customer Name : " + selected);
                    tvBBCustomerName.setText("Customer Name : " + selected);
                    loadTotalData(selected);
                    loadPaidReceivedAmount(selected);
                    loadRemainingAmount();
                }
            }
        });

        return view;
    }

    private void setDropDownList() {
        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .whereNotEqualTo("name", false)
                .orderBy("name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.size() == 0) {
                            spCustomerList.setText("No User Found");
                        } else {
                            List<String> names = new ArrayList<>();
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                String name = documentChange.getDocument().getData().get("name").toString();
                                names.add(name);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, names);
                            spCustomerList.setAdapter(adapter);
                        }
                    }
                });
    }

    private void loadTotalData(String customer_name) {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_buy_sell")
                .whereEqualTo("user", customer_name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int total_payable = 0;
                        int total_receivable = 0;

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String type = documentChange.getDocument().getData().get("type").toString();

                            int total = Integer.parseInt(documentChange.getDocument().getData().get("total").toString());

                            if (type.equals("buy")) {
                                total_receivable += total;
                            } else if (type.equals("sell")) {
                                total_payable += total;
                            }
                        }

                        tvSBTotalPayableAmount.setText("Total Payable Amount : " + total_payable);
                        tvBBTotalReceivableAmount.setText("Total Receivable Amount : " + total_receivable);
                    }
                });
    }

    private void loadPaidReceivedAmount(String customer_name) {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_transaction")
                .whereEqualTo("customer", customer_name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int paid = 0;
                        int received = 0;
                        Log.d("tag", String.valueOf(value.size()));
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String type = documentChange.getDocument().getData().get("type").toString();
                            int amount = Integer.parseInt(documentChange.getDocument().getData().get("amount").toString());

                            if (type.equals("Buyer")) {
                                paid += amount;
                            } else if (type.equals("Seller")) {
                                received += amount;
                            }

                        }

                        tvSBPaidAmount.setText("Paid Amount : " + paid);
                        tvBBReceivedAmount.setText("Received Amount : " + received);
                    }
                });
    }

    private void loadRemainingAmount() {
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                int sb_total_payable, sb_paid;
                int bb_total_receivable, bb_received;
                sb_total_payable = Integer.parseInt(tvSBTotalPayableAmount.getText().toString().split(":", 2)[1].trim());
                sb_paid = Integer.parseInt(tvSBPaidAmount.getText().toString().split(":", 2)[1].trim());
                tvSBRemainingAmount.setText("Remaining Amount : " + (sb_total_payable - sb_paid));

                bb_total_receivable = Integer.parseInt(tvBBTotalReceivableAmount.getText().toString().split(":", 2)[1].trim());
                bb_received = Integer.parseInt(tvBBReceivedAmount.getText().toString().split(":", 2)[1].trim());
                tvBBRemainingAmount.setText("Remaining Amount : " + (bb_total_receivable - bb_received));
            }
        }, 5000);
    }


}