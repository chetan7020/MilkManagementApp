package com.example.milkmanagementapp.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.milkmanagementapp.R;
import com.example.milkmanagementapp.owner.bill.CustomerBillFragment;
import com.example.milkmanagementapp.owner.bill.PaymentDetailFragment;
import com.example.milkmanagementapp.owner.bill.TransactionFragment;
import com.example.milkmanagementapp.owner.buy_sell.BuyFragment;
import com.example.milkmanagementapp.owner.buy_sell.BuySummaryFragment;
import com.example.milkmanagementapp.owner.buy_sell.SellFragment;
import com.example.milkmanagementapp.owner.buy_sell.SellSummaryFragment;
import com.google.android.material.tabs.TabLayout;

public class BillFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;

    private void initialize() {
        tabLayout = view.findViewById(R.id.tab_layout);
        frameLayout = view.findViewById(R.id.frame_layout);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_bill, container, false);

        initialize();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, new PaymentDetailFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addTab(tabLayout.newTab().setText("Payment Detail"));
        tabLayout.addTab(tabLayout.newTab().setText("Customer Bill"));
        tabLayout.addTab(tabLayout.newTab().setText("Transaction"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PaymentDetailFragment();
                        break;
                    case 1:
                        fragment = new CustomerBillFragment();
                        break;
                    case 2:
                        fragment = new TransactionFragment();
                        break;
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_layout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}