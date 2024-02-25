package com.example.fitnessstudio.user_interface_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.blood_pressure.BloodPressureActivity;

public class BloodPressureScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blood_pressure_screen, container, false);
        AppCompatButton startTracking=view.findViewById(R.id.start_tracking_blood_pressure);
        startTracking.setOnClickListener(event->{
            Intent intent=new Intent(getContext(), BloodPressureActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                FragmentManager fragmentManager=getParentFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_user_interface,new MainUserInterface());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}