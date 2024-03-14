package com.example.fitnessstudio.user_interface_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.event.EventActivityScreen;
import com.example.fitnessstudio.pedometer.Pedometer;
import com.example.fitnessstudio.report_generation.ReportGeneration;

public class MainUserInterface extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main_user_interface, container, false);
        LinearLayout bloodPressure=view.findViewById(R.id.blood_pressure);
        bloodPressure.setOnClickListener(event->{
            ReportGeneration.isReport=false;
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface,new BloodPressureScreen()).commit();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        LinearLayout heartRate=view.findViewById(R.id.heart_rate);
        heartRate.setOnClickListener(event->{
            ReportGeneration.isReport=false;
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface,new HeartRateScreen()).commit();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        LinearLayout events=view.findViewById(R.id.event);
        events.setOnClickListener(event->{
            ReportGeneration.isReport=false;
            Intent intent=new Intent(requireActivity(), EventActivityScreen.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        LinearLayout alarm=view.findViewById(R.id.alarm);
        alarm.setOnClickListener(event->{
            ReportGeneration.isReport=false;
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        LinearLayout pedometer=view.findViewById(R.id.pedometer);
        pedometer.setOnClickListener(event-> {
            ReportGeneration.isReport=false;
            Intent intent = new Intent(requireActivity(),Pedometer.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        LinearLayout reportGeneration=view.findViewById(R.id.report);
        reportGeneration.setOnClickListener(event->{
            ReportGeneration.isReport=true;
            Intent intent = new Intent(requireActivity(), ReportGeneration.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
        return view;
    }
}