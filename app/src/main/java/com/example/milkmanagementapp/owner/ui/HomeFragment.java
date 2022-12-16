package com.example.milkmanagementapp.owner.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private View view;
    private TextView tvDate, tvActiveSellerNumber, tvActiveBuyerNumber, tvTotalActiveCustomerNumber;

    private Button btnNewTransaction;

    private TableLayout tlDataCollection, tlDataDistribution;

    private int liter_morning_collection = 0, avg_fat_morning_collection = 0, avg_rate_morning_collection = 0, amount_morning_collection = 0;
    private int liter_evening_collection = 0, avg_fat_evening_collection = 0, avg_rate_evening_collection = 0, amount_evening_collection = 0;

    private int liter_morning_distribution = 0, avg_fat_morning_distribution = 0, avg_rate_morning_distribution = 0, amount_morning_distribution = 0;
    private int liter_evening_distribution = 0, avg_fat_evening_distribution = 0, avg_rate_evening_distribution = 0, amount_evening_distribution = 0;

    private int count_collection_morning = 0, count_collection_evening = 0, count_distribution_morning = 0, count_distribution_evening = 0;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private void initialize() {
        tvDate = view.findViewById(R.id.tvDate);
        tvActiveSellerNumber = view.findViewById(R.id.tvActiveSellerNumber);
        tvActiveBuyerNumber = view.findViewById(R.id.tvActiveBuyerNumber);
        tvTotalActiveCustomerNumber = view.findViewById(R.id.tvTotalActiveCustomerNumber);

        btnNewTransaction = view.findViewById(R.id.btnNewTransaction);

        tlDataCollection = view.findViewById(R.id.tlDataCollection);
        tlDataDistribution = view.findViewById(R.id.tlDataDistribution);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_home, container, false);

        initialize();

        tvDate.setText("Date : " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        setCustomerCount();

        setCollectionDistribution();

        Dialog dialog = new Dialog(getActivity());
        btnNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.owner_main_add_transaction_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                RadioGroup rgBuyerSellerTransaction = dialog.findViewById(R.id.rgBuyerSeller);
                AutoCompleteTextView spCustomerListTransaction = dialog.findViewById(R.id.spCustomerList);
                TextInputEditText etAmountTransaction = dialog.findViewById(R.id.etAmount);
                Button btnSaveTransaction = dialog.findViewById(R.id.btnSave);

                String type = "";
                if (rgBuyerSellerTransaction.getCheckedRadioButtonId() == R.id.rbBuyer) {
                    type = "Buyer";
                } else if (rgBuyerSellerTransaction.getCheckedRadioButtonId() == R.id.rbSeller) {
                    type = "Seller";
                }

                firebaseFirestore
                        .collection(firebaseUser.getPhoneNumber() + "_customer")
                        .whereNotEqualTo("name", false)
//                        .whereEqualTo("type", type)
                        .orderBy("name")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value.size() == 0) {
                                    spCustomerListTransaction.setText("No User Found");
                                } else {
                                    List<String> names = new ArrayList<>();
                                    names.add("Select Customer");
                                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                                        String name = documentChange.getDocument().getData().get("name").toString();
                                        String id = documentChange.getDocument().getData().get("id").toString();
                                        names.add(id + " - " + name);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, names);
                                    spCustomerListTransaction.setAdapter(adapter);
                                }
                            }
                        });


                btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selected_user = spCustomerListTransaction.getEditableText().toString();
                        String[] arrOfStr = selected_user.split("-", 2);
                        selected_user = arrOfStr[1].trim();
                        String id = arrOfStr[0].trim();
                        String amount = etAmountTransaction.getText().toString().trim();

                        String type = "";
                        if (rgBuyerSellerTransaction.getCheckedRadioButtonId() == R.id.rbBuyer) {
                            type = "Buyer";
                        } else if (rgBuyerSellerTransaction.getCheckedRadioButtonId() == R.id.rbSeller) {
                            type = "Seller";
                        }

                        Map<Object, String> data = new HashMap<>();

                        data.put("id", id);
                        data.put("customer", selected_user);
                        data.put("amount", amount);
                        data.put("type", type);
                        data.put("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date())) ;

                        firebaseFirestore
                                .collection(firebaseUser.getPhoneNumber() + "_transaction")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        etAmountTransaction.setText("");
                                        spCustomerListTransaction.setText("Select Customer");

                                        Toast.makeText(getActivity(), "Record Submitted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Failed to Submit Record", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        return view;
    }


    private void setCollectionDistribution() {
        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_buy_sell")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String time = documentChange.getDocument().getData().get("time").toString();
                            String type = documentChange.getDocument().getData().get("type").toString();

                            if (time.equals("Morning") && type.equals("buy")) {
                                count_collection_morning += 1;

                                liter_morning_collection += Integer.parseInt(documentChange.getDocument().getData().get("liter").toString());
                                avg_fat_morning_collection += Integer.parseInt(documentChange.getDocument().getData().get("fat").toString());
                                avg_rate_morning_collection += Integer.parseInt(documentChange.getDocument().getData().get("rate").toString());
                                amount_morning_collection += Integer.parseInt(documentChange.getDocument().getData().get("total").toString());
                            } else if (time.equals("Evening") && type.equals("buy")) {
                                count_collection_evening += 1;

                                liter_evening_collection += Integer.parseInt(documentChange.getDocument().getData().get("liter").toString());
                                avg_fat_evening_collection += Integer.parseInt(documentChange.getDocument().getData().get("fat").toString());
                                avg_rate_evening_collection += Integer.parseInt(documentChange.getDocument().getData().get("rate").toString());
                                amount_evening_collection += Integer.parseInt(documentChange.getDocument().getData().get("total").toString());
                            }


                            if (time.equals("Morning") && type.equals("sell")) {
                                count_distribution_morning += 1;

                                liter_morning_distribution += Integer.parseInt(documentChange.getDocument().getData().get("liter").toString());
                                avg_fat_morning_distribution += Integer.parseInt(documentChange.getDocument().getData().get("fat").toString());
                                avg_rate_morning_distribution += Integer.parseInt(documentChange.getDocument().getData().get("rate").toString());
                                amount_morning_distribution += Integer.parseInt(documentChange.getDocument().getData().get("total").toString());
                            } else if (time.equals("Evening") && type.equals("sell")) {
                                count_distribution_evening += 1;

                                liter_evening_distribution += Integer.parseInt(documentChange.getDocument().getData().get("liter").toString());
                                avg_fat_evening_distribution += Integer.parseInt(documentChange.getDocument().getData().get("fat").toString());
                                avg_rate_evening_distribution += Integer.parseInt(documentChange.getDocument().getData().get("rate").toString());
                                amount_evening_distribution += Integer.parseInt(documentChange.getDocument().getData().get("total").toString());
                            }

                        }

                        if (count_collection_morning != 0) {
                            avg_fat_morning_collection = avg_fat_morning_collection / count_collection_morning;
                        }
                        if (count_collection_evening != 0) {
                            avg_fat_evening_collection = avg_fat_evening_collection / count_collection_evening;
                        }
                        if (count_distribution_morning != 0) {
                            avg_rate_morning_distribution = avg_rate_morning_distribution / count_distribution_morning;
                        }
                        if (count_distribution_evening != 0) {
                            avg_rate_evening_distribution = avg_rate_evening_distribution / count_distribution_evening;
                        }


                        String table_time[] = {"Morning", "Evening", "Total"};

                        for (String s : table_time) {
                            View row_view = getLayoutInflater().inflate(R.layout.owner_home_table_row_layout, null, false);

                            TextView tvTime, tvLiter, tvAvgFat, tvAvgRate, tvAmount;

                            tvTime = row_view.findViewById(R.id.tvTime);
                            tvLiter = row_view.findViewById(R.id.tvLiter);
                            tvAvgFat = row_view.findViewById(R.id.tvAvgFat);
                            tvAvgRate = row_view.findViewById(R.id.tvAvgRate);
                            tvAmount = row_view.findViewById(R.id.tvAmount);

                            if (s.equals("Morning")) {
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_morning_collection));
                                tvAvgFat.setText(String.valueOf(avg_fat_morning_collection));
                                tvAvgRate.setText(String.valueOf(avg_rate_morning_collection));
                                tvAmount.setText(String.valueOf(amount_morning_collection));

                                tlDataCollection.addView(row_view);
                            } else if (s.equals("Evening")) {
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_evening_collection));
                                tvAvgFat.setText(String.valueOf(avg_fat_evening_collection));
                                tvAvgRate.setText(String.valueOf(avg_rate_evening_collection));
                                tvAmount.setText(String.valueOf(amount_evening_collection));

                                tlDataCollection.addView(row_view);
                            } else if (s.equals("Total")) {
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_morning_collection + liter_evening_collection));
                                tvAvgFat.setText(String.valueOf((avg_fat_morning_collection + avg_fat_evening_collection) / 2));
                                tvAvgRate.setText(String.valueOf((avg_rate_morning_collection + avg_rate_evening_collection) / 2));
                                tvAmount.setText(String.valueOf(amount_morning_collection + amount_evening_collection));

                                tlDataCollection.addView(row_view);
                            }
                        }

                        for (String s : table_time){
                            View row_view = getLayoutInflater().inflate(R.layout.owner_home_table_row_layout, null, false);

                            TextView tvTime, tvLiter, tvAvgFat, tvAvgRate, tvAmount;

                            tvTime = row_view.findViewById(R.id.tvTime);
                            tvLiter = row_view.findViewById(R.id.tvLiter);
                            tvAvgFat = row_view.findViewById(R.id.tvAvgFat);
                            tvAvgRate = row_view.findViewById(R.id.tvAvgRate);
                            tvAmount = row_view.findViewById(R.id.tvAmount);

                            if (s.equals("Morning")){
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_morning_distribution));
                                tvAvgFat.setText(String.valueOf(avg_fat_morning_distribution));
                                tvAvgRate.setText(String.valueOf(avg_rate_morning_distribution));
                                tvAmount.setText(String.valueOf(amount_morning_distribution));

                                tlDataDistribution.addView(row_view);
                            }else if (s.equals("Evening")){
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_evening_distribution));
                                tvAvgFat.setText(String.valueOf(avg_fat_evening_distribution));
                                tvAvgRate.setText(String.valueOf(avg_rate_evening_distribution));
                                tvAmount.setText(String.valueOf(amount_evening_distribution));

                                tlDataDistribution.addView(row_view);
                            }else if (s.equals("Total")){
                                tvTime.setText(s);
                                tvLiter.setText(String.valueOf(liter_morning_distribution + liter_evening_distribution));
                                tvAvgFat.setText(String.valueOf( (avg_fat_morning_distribution + avg_fat_evening_distribution) / 2 ));
                                tvAvgRate.setText(String.valueOf( (avg_rate_morning_collection + avg_rate_evening_distribution) / 2 ));
                                tvAmount.setText(String.valueOf(amount_morning_distribution + amount_evening_distribution));

                                tlDataDistribution.addView(row_view);
                            }
                        }

                    }
                });

    }

    private void setCustomerCount() {
        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .document("active")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvActiveSellerNumber.setText(documentSnapshot.getString("total_seller_count"));
                        tvActiveBuyerNumber.setText(documentSnapshot.getString("total_buyer_count"));
                        tvTotalActiveCustomerNumber.setText(documentSnapshot.getString("total_count"));
                    }
                });
    }


}