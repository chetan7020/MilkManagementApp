package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnNext;
    private TextInputEditText etMobileNumber;

    private void initialize() {
        btnNext = findViewById(R.id.btnNext);

        etMobileNumber = findViewById(R.id.etMobileNumber);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile_number = etMobileNumber.getText().toString().trim();

                if (TextUtils.isEmpty(mobile_number)) {
                    Toast.makeText(LoginActivity.this, "Mobile Number is Required", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this , VerifyOtpActivity.class);
                    intent.putExtra("mobile_number" , mobile_number);
                    startActivity(intent);
                }
            }
        });
    }

}
