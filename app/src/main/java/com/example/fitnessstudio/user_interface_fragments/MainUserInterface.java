package com.example.fitnessstudio.user_interface_fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.event.EventActivityScreen;

public class MainUserInterface extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main_user_interface, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout bloodPressure=view.findViewById(R.id.blood_pressure);
        bloodPressure.setOnClickListener(event->{
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface,new BloodPressureScreen()).commit();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout heartRate=view.findViewById(R.id.heart_rate);
        heartRate.setOnClickListener(event->{
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface,new HeartRateScreen()).commit();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout events=view.findViewById(R.id.event);
        events.setOnClickListener(event->{
            Intent intent=new Intent(requireActivity(), EventActivityScreen.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        return view;
    }
}