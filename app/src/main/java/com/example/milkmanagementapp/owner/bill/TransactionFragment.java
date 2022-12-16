package com.example.milkmanagementapp.owner.bill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
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

public class TransactionFragment extends Fragment {

    private View view;
    private AutoCompleteTextView spCustomerList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btnSearch;
    private TableLayout tlTransaction;

    private void initialize() {
        spCustomerList = view.findViewById(R.id.spCustomerList);
        btnSearch = view.findViewById(R.id.btnSearch);

        tlTransaction = view.findViewById(R.id.tlTransaction);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_bill_fragment_transaction, container, false);

        initialize();

        setDropDownList();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected = spCustomerList.getEditableText().toString();

                if (selected.equals("Select Customer")) {
                    Toast.makeText(getActivity(), "Select Customer First", Toast.LENGTH_SHORT).show();
                } else {
                    tlTransaction.removeAllViews();
                    loadData(selected);
                }
            }
        });


        return view;
    }

    private void loadData(String customer_name) {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_transaction")
                .whereEqualTo("customer", customer_name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()) {
                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                String date = documentChange.getDocument().getData().get("date").toString();
                                String amount = documentChange.getDocument().getData().get("amount").toString();

                                addRow(date, amount);
                            }
                        }
                    }
                });
    }

    private void addRow(String date, String amount) {
        View view = getLayoutInflater().inflate(R.layout.owner_bill_transaction_table_row_layout, null, false);

        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvAmount = view.findViewById(R.id.tvAmount);

        tvDate.setText(date);
        tvAmount.setText(amount);

        tlTransaction.addView(view);
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

}