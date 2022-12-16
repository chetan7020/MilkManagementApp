package com.example.milkmanagementapp.customer.buy_sell;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milkmanagementapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SellFragment extends Fragment {

    private View view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvLiter, tvFat, tvRate, tvTotal, tvDairyName, tvDate;
    private TableLayout tlDataMorning, tlDataEvening;
    private RadioGroup rgCowBuffalo;
    private SearchView svSearch;
    String animal_rb = "", user_main = "";

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tlDataMorning = view.findViewById(R.id.tlDataMorning);
        tlDataEvening = view.findViewById(R.id.tlDataEvening);

        rgCowBuffalo = view.findViewById(R.id.rgCowBuffalo);

        svSearch = view.findViewById(R.id.svSearch);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_buysell_fragment_sell, container, false);

        initialize();

        firebaseFirestore.collection("customer")
                .whereEqualTo("mobile_number", firebaseUser.getPhoneNumber())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String name = documentChange.getDocument().getData().get("name").toString();

                            user_main = name.trim();
                        }
                    }
                });


        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                for (int i = 1; i < tlDataMorning.getChildCount(); i++) {

                    TableRow trData = tlDataMorning.getChildAt(i).findViewById(R.id.trData);
                    TextView tvDairyName = tlDataMorning.getChildAt(i).findViewById(R.id.tvDairyName);
                    TextView tvDate = tlDataMorning.getChildAt(i).findViewById(R.id.tvDate);

                    if (!(tvDairyName.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvDate.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        trData.setVisibility(View.GONE);
                    } else {
                        trData.setVisibility(View.VISIBLE);
                    }

                }

                for (int i = 1; i < tlDataEvening.getChildCount(); i++) {

                    TableRow trData = tlDataEvening.getChildAt(i).findViewById(R.id.trData);
                    TextView tvDairyName = tlDataEvening.getChildAt(i).findViewById(R.id.tvDairyName);
                    TextView tvDate = tlDataEvening.getChildAt(i).findViewById(R.id.tvDate);

                    if (!(tvDairyName.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvDate.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        trData.setVisibility(View.GONE);
                    } else {
                        trData.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }
        });


        animal_rb = "Cow";

        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber() + "_customer")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();
                            String dairy_name = documentChange.getDocument().getData().get("dairy_name").toString();
                            loadData(mobile_number, dairy_name);
                        }
                    }
                });

        rgCowBuffalo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                tlDataMorning.removeAllViews();
                tlDataEvening.removeAllViews();

                if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbCow) {
                    animal_rb = "Cow";
                } else if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbBuffalo) {
                    animal_rb = "Buffalo";
                }

                firebaseFirestore
                        .collection(firebaseUser.getPhoneNumber() + "_customer")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    String mobile_number = documentChange.getDocument().getData().get("mobile_number").toString();
                                    String dairy_name = documentChange.getDocument().getData().get("dairy_name").toString();
                                    loadData(mobile_number, dairy_name);
                                }
                            }
                        });

            }
        });

        return view;
    }

    private void loadData(String mobile_number, String dairy_name) {
        firebaseFirestore.collection(mobile_number + "_buy_sell")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d("tmr", String.valueOf(value.size()));
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String liter = documentChange.getDocument().getData().get("liter").toString();
                            String fat = documentChange.getDocument().getData().get("fat").toString();
                            String rate = documentChange.getDocument().getData().get("rate").toString();
                            String total = documentChange.getDocument().getData().get("total").toString();
                            String date = documentChange.getDocument().getData().get("date").toString();

                            String time = documentChange.getDocument().getData().get("time").toString();
                            String type = documentChange.getDocument().getData().get("type").toString();
                            String animal = documentChange.getDocument().getData().get("animal").toString();
                            String user = documentChange.getDocument().getData().get("user").toString();

                            if (user.equals(user_main)) {
                                if (time.equals("Morning") && type.equals("sell") && animal.equals(animal_rb)) {
                                    addMorning(dairy_name, liter, fat, rate, total, date);
                                } else if (time.equals("Evening") && type.equals("sell") && animal.equals(animal_rb)) {
                                    addEvening(dairy_name, liter, fat, rate, total, date);
                                }
                            }
                        }
                    }
                });

    }

    private void addEvening(String dairy_name, String liter, String fat, String rate, String total, String date) {
        View view = getLayoutInflater().inflate(R.layout.customer_buysell_table_row_layout, null, false);

        tvDairyName = view.findViewById(R.id.tvDairyName);
        tvLiter = view.findViewById(R.id.tvLiter);
        tvFat = view.findViewById(R.id.tvFat);
        tvRate = view.findViewById(R.id.tvRate);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDate = view.findViewById(R.id.tvDate);

        tvDairyName.setText(dairy_name);
        tvLiter.setText(liter);
        tvFat.setText(fat);
        tvRate.setText(rate);
        tvTotal.setText(total);
        tvDate.setText(date);

        tlDataEvening.addView(view);
    }

    private void addMorning(String dairy_name, String liter, String fat, String rate, String total, String date) {
        View view = getLayoutInflater().inflate(R.layout.customer_buysell_table_row_layout, null, false);

        tvDairyName = view.findViewById(R.id.tvDairyName);
        tvLiter = view.findViewById(R.id.tvLiter);
        tvFat = view.findViewById(R.id.tvFat);
        tvRate = view.findViewById(R.id.tvRate);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDate = view.findViewById(R.id.tvDate);

        tvDairyName.setText(dairy_name);
        tvLiter.setText(liter);
        tvFat.setText(fat);
        tvRate.setText(rate);
        tvTotal.setText(total);
        tvDate.setText(date);

        tlDataMorning.addView(view);
    }
}
