package com.example.milkmanagementapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkmanagementapp.owner.OwnerMainActivity;
import com.example.milkmanagementapp.R;

public class LoginSuccessesActivity extends AppCompatActivity {

    private Button btnNext;

    private void initialize() {
        btnNext = findViewById(R.id.btnNext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successed);

        initialize();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSuccessesActivity.this , SelectUserActivity.class));
            }
        });
    }

}
