package com.example.milkmanagementapp.owner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.databinding.ActivityOwnerMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerMainActivity extends AppCompatActivity {

    private ActivityOwnerMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOwnerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_customer, R.id.navigation_buySell, R.id.navigation_home,
                R.id.navigation_bill, R.id.navigation_products
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_owner_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}