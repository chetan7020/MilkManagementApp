package com.example.milkmanagementapp.owner.buy_sell;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SellFragment extends Fragment {

    private View view;
    private TextInputEditText etDate;
    private AutoCompleteTextView spCustomerList;

    private void initialize() {
        etDate = view.findViewById(R.id.etDate);
        spCustomerList = view.findViewById(R.id.spCustomerList);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_customer_fragment_buy, container, false);

        initialize();

        etDate  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        String[] items = new String[]{"Select Customer" , "Chetan Patil", "Dagaji Patil", "Sunanda Patil"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spCustomerList.setAdapter(adapter);

        return view;
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
