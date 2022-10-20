package com.example.milkmanagementapp.owner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.databinding.ActivityOwnerMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class OwnerMainActivity extends AppCompatActivity {

    private ActivityOwnerMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOwnerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber()+"_customer")
                .whereEqualTo("status" , "Active")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Map<Object, String> data = new HashMap<>();

                        data.put("count", String.valueOf(value.size()));

                        firebaseFirestore
                                .collection(firebaseUser.getPhoneNumber()+"_customer")
                                .document("active")
                                .set(data);

                    }
                });

        firebaseFirestore
                .collection(firebaseUser.getPhoneNumber()+"_customer")
                .whereEqualTo("status" , "InActive")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Map<Object, String> data = new HashMap<>();

                        data.put("count", String.valueOf(value.size()));

                        firebaseFirestore
                                .collection(firebaseUser.getPhoneNumber()+"_customer")
                                .document("inactive")
                                .set(data);
                    }
                });

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