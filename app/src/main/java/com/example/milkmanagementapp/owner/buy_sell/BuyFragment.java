package com.example.milkmanagementapp.owner.buy_sell;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyFragment extends Fragment {

    private View view;
    private TextInputEditText etDate;
    private AutoCompleteTextView spCustomerList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btnSave;
    private EditText etLiter, etFat, etRate, etTotal;
    private RadioGroup rgCowBuffalo, rgMorningEvening;

    private void initialize() {
        etLiter = view.findViewById(R.id.etLiter);
        etFat = view.findViewById(R.id.etFat);
        etRate = view.findViewById(R.id.etRate);
        etTotal = view.findViewById(R.id.etTotal);

        rgCowBuffalo = view.findViewById(R.id.rgCowBuffalo);
        rgMorningEvening = view.findViewById(R.id.rgMorningEvening);


        etDate = view.findViewById(R.id.etDate);

        spCustomerList = view.findViewById(R.id.spCustomerList);

        btnSave = view.findViewById(R.id.btnSave);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_customer_fragment_buy, container, false);

        initialize();


        setDropDownList();

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String liter = etLiter.getText().toString().trim();
                String rate = etRate.getText().toString().trim();

                if (liter.equals("") || rate.equals("")) {

                    if (liter.equals("")) {
                        liter = "0";
                    }

                    if (rate.equals("")) {
                        rate = "0";
                    }

                }

                etTotal.setText(String.valueOf(Integer.parseInt(liter) * Integer.parseInt(rate)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String liter = etLiter.getText().toString().trim();
                String rate = etRate.getText().toString().trim();

                if (liter.equals("") || rate.equals("")) {

                    if (liter.equals("")) {
                        liter = "0";
                    }

                    if (rate.equals("")) {
                        rate = "0";
                    }

                }

                etTotal.setText(String.valueOf(Integer.parseInt(liter) * Integer.parseInt(rate)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    insertData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        return view;
    }

    private void insertData() throws ParseException {
        String selected_user = spCustomerList.getEditableText().toString();
        String date = etDate.getText().toString().trim();


        String animal = "";
        if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbCow) {
            animal = "Cow";
        } else if (rgCowBuffalo.getCheckedRadioButtonId() == R.id.rbBuffalo) {
            animal = "Buffalo";
        }

        String time = "";
        if (rgMorningEvening.getCheckedRadioButtonId() == R.id.rbMorning) {
            time = "Morning";
        } else if (rgMorningEvening.getCheckedRadioButtonId() == R.id.rbEvening) {
            time = "Evening";
        }

        String liter = etLiter.getText().toString().trim();
        String fat = etFat.getText().toString().trim();
        String rate = etRate.getText().toString().trim();

        if (liter.equals("") ||
                fat.equals("") ||
                rate.equals("") ||
                liter.equals("0") ||
                fat.equals("0") ||
                rate.equals("0") ||
                date.equals("")) {

            if (liter.equals("") || liter.equals("0")) {
                etLiter.setError("Required");
            }

            if (fat.equals("") || fat.equals("0")) {
                etFat.setError("Required");
            }

            if (rate.equals("") || rate.equals("0")) {
                etRate.setError("Required");
            }

            if (date.equals("")) {
                etDate.setError("Required");
            }

        } else {

            etLiter.setError(null);
            etFat.setError(null);
            etRate.setError(null);
            etDate.setError(null);

            date = date.replace("-", "/");

            Date date_new = new SimpleDateFormat("dd/MM/yyyy")
                    .parse(date);

            Timestamp ts = new Timestamp(date_new.getTime());

            SimpleDateFormat formatter
                    = new SimpleDateFormat("yyyy-MM-dd");

            date = formatter.format(ts);

            if (selected_user.equals("Select Customer") || selected_user.equals("")) {
                Toast.makeText(getActivity(), "Select Customer", Toast.LENGTH_SHORT).show();
            } else {

                String total = etTotal.getText().toString();

                Map<Object, String> data = new HashMap<>();

                data.put("date", date);
                data.put("user", selected_user);
                data.put("animal", animal);
                data.put("time", time);
                data.put("liter", liter);
                data.put("fat", fat);
                data.put("rate", rate);
                data.put("total", total);
                data.put("type", "buy");

                firebaseFirestore
                        .collection(firebaseUser.getPhoneNumber() + "_buy_sell")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                etLiter.setText("");
                                etFat.setText("");
                                etRate.setText("");
                                etTotal.setText("");

                                spCustomerList.setText("Select Customer");

                                Toast.makeText(getActivity(), "Record Submitted", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }
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
                            names.add("Select Customer");
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

    private void datePicker() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

}
