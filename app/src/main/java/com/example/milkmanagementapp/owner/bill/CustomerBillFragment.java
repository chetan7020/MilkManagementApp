package com.example.milkmanagementapp.owner.bill;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CustomerBillFragment extends Fragment {

    private View view;
    private AutoCompleteTextView spCustomerList;
    private SwitchCompat swDayInterval;
    private TextView tvTo;
    private TextInputEditText etDateFrom, etDateTo;
    private TextInputLayout etToLayout;

    private void initialize() {
        spCustomerList = view.findViewById(R.id.spCustomerList);
        swDayInterval = view.findViewById(R.id.swDayInterval);
        tvTo = view.findViewById(R.id.tvTo);
        etToLayout = view.findViewById(R.id.etToLayout);
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_bill_fragment_customer_bill, container, false);

        initialize();

        String[] items = new String[]{"Select Customer" , "Chetan Patil", "Dagaji Patil", "Sunanda Patil"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spCustomerList.setAdapter(adapter);

        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                etDateFrom.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                etDateTo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        swDayInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swDayInterval.isChecked()){
                    etToLayout.setVisibility(View.VISIBLE);
                    tvTo.setVisibility(View.VISIBLE);
                }else if (!swDayInterval.isChecked()){
                    etToLayout.setVisibility(View.GONE);
                    tvTo.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}