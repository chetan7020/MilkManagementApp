package com.example.milkmanagementapp.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.milkmanagementapp.R;

public class HomeFragment extends Fragment {

    private View view;

    private void initialize() {
    }

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_home, container, false);

        initialize();

        return view;
    }

}