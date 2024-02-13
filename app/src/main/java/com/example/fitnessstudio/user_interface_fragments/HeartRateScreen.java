package com.example.fitnessstudio.user_interface_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.heart_rate.HeartRateActivity;

public class HeartRateScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_heart_rate_screen, container, false);
        AppCompatButton startTracking=view.findViewById(R.id.start_tracking);
        startTracking.setOnClickListener(event->{
            Intent intent=new Intent(getContext(),HeartRateActivity.class);
            startActivity(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        return view;
    }
}