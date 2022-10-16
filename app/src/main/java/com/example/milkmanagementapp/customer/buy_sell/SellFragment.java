package com.example.milkmanagementapp.customer.buy_sell;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milkmanagementapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SellFragment extends Fragment {

    private View view;
    private SwitchCompat swDayInterval;
    private TextView tvTo;
    private TextInputEditText etDateFrom, etDateTo;
    private TextInputLayout etToLayout;

    private void initialize() {
        swDayInterval = view.findViewById(R.id.swDayInterval);
        tvTo = view.findViewById(R.id.tvTo);
        etToLayout = view.findViewById(R.id.etToLayout);
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_buysell_fragment_sell, container, false);

        initialize();

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
