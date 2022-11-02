package com.example.milkmanagementapp.owner.buy_sell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SellSummaryFragment extends Fragment {

    private View view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvCustomerName, tvLiter, tvFat, tvRate, tvTotal, tvDate;
    private TableLayout tlDataMorning, tlDataEvening;
    private RadioGroup rgCowBuffalo;
    private SearchView svSearch;
    String animal_rb = "";

//    private SwitchCompat swDayInterval;
//    private TextView tvTo;
//    private TextInputEditText etDateFrom, etDateTo;
//    private TextInputLayout etToLayout;

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tlDataMorning = view.findViewById(R.id.tlDataMorning);
        tlDataEvening = view.findViewById(R.id.tlDataEvening);

        svSearch = view.findViewById(R.id.svSearch);

        rgCowBuffalo = view.findViewById(R.id.rgCowBuffalo);

//        swDayInterval = view.findViewById(R.id.swDayInterval);
//        tvTo = view.findViewById(R.id.tvTo);
//        etToLayout = view.findViewById(R.id.etToLayout);
//        etDateFrom = view.findViewById(R.id.etDateFrom);
//        etDateTo = view.findViewById(R.id.etDateTo);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_customer_fragment_sell_summary, container, false);

        initialize();

        if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbCow) {
            animal_rb = "Cow";
        } else if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbBuffalo) {
            animal_rb = "Buffalo";
        }

        loadData();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                for (int i = 1; i < tlDataMorning.getChildCount(); i++) {

                    TableRow trData = tlDataMorning.getChildAt(i).findViewById(R.id.trData);
                    TextView tvCustomerName = tlDataMorning.getChildAt(i).findViewById(R.id.tvCustomerName);
                    TextView tvDate = tlDataMorning.getChildAt(i).findViewById(R.id.tvDate);

                    if (!(tvCustomerName.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvDate.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        trData.setVisibility(View.GONE);
                    } else {
                        trData.setVisibility(View.VISIBLE);
                    }

                }

                for (int i = 1; i < tlDataEvening.getChildCount(); i++) {

                    TableRow trData = tlDataEvening.getChildAt(i).findViewById(R.id.trData);
                    TextView tvCustomerName = tlDataEvening.getChildAt(i).findViewById(R.id.tvCustomerName);
                    TextView tvDate = tlDataEvening.getChildAt(i).findViewById(R.id.tvDate);

                    if (!(tvCustomerName.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvDate.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        trData.setVisibility(View.GONE);
                    } else {
                        trData.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }
        });

        rgCowBuffalo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                tlDataMorning.removeAllViews();
                tlDataEvening.removeAllViews();

                View viewMorning = getLayoutInflater().inflate(R.layout.owner_buysell_summary_morning_table_header_layout, null, false);
                View viewEvening = getLayoutInflater().inflate(R.layout.owner_buysell_summary_evening_table_header_layout, null, false);

                tlDataMorning.addView(viewMorning);
                tlDataEvening.addView(viewEvening);

                if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbCow) {
                    animal_rb = "Cow";
                } else if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbBuffalo) {
                    animal_rb = "Buffalo";
                }

                loadData();
            }
        });


//        etDateFrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Calendar.getInstance();
//
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        getContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                etDateFrom.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                            }
//                        },
//                        year, month, day);
//                datePickerDialog.show();
//            }
//        });

//        etDateTo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Calendar.getInstance();
//
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        getContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                etDateTo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                            }
//                        },
//                        year, month, day);
//                datePickerDialog.show();
//            }
//        });

//        swDayInterval.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (swDayInterval.isChecked()){
//                    etToLayout.setVisibility(View.VISIBLE);
//                    tvTo.setVisibility(View.VISIBLE);
//                }else if (!swDayInterval.isChecked()){
//                    etToLayout.setVisibility(View.GONE);
//                    tvTo.setVisibility(View.GONE);
//                }
//            }
//        });

        return view;
    }

    private void addEvening(String name, String liter, String fat, String rate, String total, String date) {
        View view = getLayoutInflater().inflate(R.layout.owner_buysell_summary_table_row_layout, null, false);

        tvCustomerName = view.findViewById(R.id.tvCustomerName);
        tvLiter = view.findViewById(R.id.tvLiter);
        tvFat = view.findViewById(R.id.tvFat);
        tvRate = view.findViewById(R.id.tvRate);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDate = view.findViewById(R.id.tvDate);

        tvCustomerName.setText(name);
        tvLiter.setText(liter);
        tvFat.setText(fat);
        tvRate.setText(rate);
        tvTotal.setText(total);
        tvDate.setText(date);

        tlDataEvening.addView(view);
    }

    private void addMorning(String name, String liter, String fat, String rate, String total, String date) {
        View view = getLayoutInflater().inflate(R.layout.owner_buysell_summary_table_row_layout, null, false);

        tvCustomerName = view.findViewById(R.id.tvCustomerName);
        tvLiter = view.findViewById(R.id.tvLiter);
        tvFat = view.findViewById(R.id.tvFat);
        tvRate = view.findViewById(R.id.tvRate);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDate = view.findViewById(R.id.tvDate);

        tvCustomerName.setText(name);
        tvLiter.setText(liter);
        tvFat.setText(fat);
        tvRate.setText(rate);
        tvTotal.setText(total);
        tvDate.setText(date);

        tlDataMorning.addView(view);
    }

    private void loadData() {
        firebaseFirestore.collection(firebaseUser.getPhoneNumber() + "_buy_sell")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String name = documentChange.getDocument().getData().get("user").toString();
                            String liter = documentChange.getDocument().getData().get("liter").toString();
                            String fat = documentChange.getDocument().getData().get("fat").toString();
                            String rate = documentChange.getDocument().getData().get("rate").toString();
                            String total = documentChange.getDocument().getData().get("total").toString();
                            String date = documentChange.getDocument().getData().get("date").toString();

                            String time = documentChange.getDocument().getData().get("time").toString();
                            String type = documentChange.getDocument().getData().get("type").toString();
                            String animal = documentChange.getDocument().getData().get("animal").toString();

                            if (time.equals("Morning") && type.equals("sell") && animal.equals(animal_rb)) {
                                addMorning(name, liter, fat, rate, total, date);
                            } else if (time.equals("Evening") && type.equals("sell") && animal.equals(animal_rb)) {
                                addEvening(name, liter, fat, rate, total, date);
                            }
                        }
                    }
                });

    }


}
