package com.example.milkmanagementapp.owner.bill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class PaymentDetailFragment extends Fragment {

    private View view;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private TextView tvSBTotalPayableAmount, tvSBPaidAmount, tvSBRemainingAmount;
    private TextView tvBBTotalReceivableAmount, tvBBReceivedAmount, tvBBRemainingAmount;

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvSBTotalPayableAmount = view.findViewById(R.id.tvSBTotalPayableAmount);
        tvSBPaidAmount = view.findViewById(R.id.tvSBPaidAmount);
        tvSBRemainingAmount = view.findViewById(R.id.tvSBRemainingAmount);

        tvBBTotalReceivableAmount = view.findViewById(R.id.tvBBTotalReceivableAmount);
        tvBBReceivedAmount = view.findViewById(R.id.tvBBReceivedAmount);
        tvBBRemainingAmount = view.findViewById(R.id.tvBBRemainingAmount);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_bill_fragment_payment_detail, container, false);

        initialize();

        loadTotalData();

        loadPaidReceivedAmount();

        loadRemainingAmount();

        return view;
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

    private void loadPaidReceivedAmount() {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_transaction")
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

    private void loadTotalData() {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_buy_sell")
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
}