package com.example.milkmanagementapp.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milkmanagementapp.R;

public class ProfileFragment extends Fragment {

    private View view;

    private void initialize() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.owner_fragment_profile, container, false);

        initialize();

        return view;
    }

}