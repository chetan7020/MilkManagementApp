package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.R;

public class VerifyOtpActivity extends AppCompatActivity {

    private Button btnVerify;

    private void initialize() {
        btnVerify = findViewById(R.id.btnVerify);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initialize();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VerifyOtpActivity.this , LoginSuccessesActivity.class));
            }
        });
    }

}
