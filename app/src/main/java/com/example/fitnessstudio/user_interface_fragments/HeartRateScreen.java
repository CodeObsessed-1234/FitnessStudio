package com.example.fitnessstudio.user_interface_fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitnessstudio.R;

public class HeartRateScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_heart_rate_screen, container, false);
        TextView quoteTextView=view.findViewById(R.id.quote_text_view_heart_rate);
        AppCompatButton addToReport=view.findViewById(R.id.add_to_report_heart_rate);
        addToReport.setVisibility(View.GONE);
        AppCompatButton startTracking=view.findViewById(R.id.start_tracking);
        startTracking.setOnClickListener(event->{
            startTracking.setEnabled(false);

        });
        return view;
    }
}