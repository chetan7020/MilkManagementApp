package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.customer.CustomerMainActivity;
import com.example.milkmanagementapp.owner.OwnerMainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SelectUserActivity extends AppCompatActivity {

    private Button btnNext;
    private RadioGroup rgUserType;
    private TextInputLayout etShopNameLayout;
    private RadioButton rbCustomer, rbDairyOwner;

    private void initialize() {
        btnNext = findViewById(R.id.btnNext);
        rgUserType = findViewById(R.id.rgUserType);
        etShopNameLayout = findViewById(R.id.etShopNameLayout);
        rbCustomer = findViewById(R.id.rbCustomer);
        rbDairyOwner = findViewById(R.id.rbDairyOwner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        initialize();

        rbCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShopNameLayout.setVisibility(View.GONE);
            }
        });

        rbDairyOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShopNameLayout.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgUserType.getCheckedRadioButtonId() == rbCustomer.getId()) {
                    startActivity(new Intent(SelectUserActivity.this, CustomerMainActivity.class));
                } else if (rgUserType.getCheckedRadioButtonId() == rbDairyOwner.getId()) {
                    startActivity(new Intent(SelectUserActivity.this, OwnerMainActivity.class));
                }
            }
        });
    }

}