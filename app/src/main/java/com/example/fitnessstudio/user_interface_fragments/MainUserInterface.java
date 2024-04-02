package com.example.fitnessstudio.user_interface_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.event.EventActivityScreen;
import com.example.fitnessstudio.pedometer.Pedometer;
import com.example.fitnessstudio.session.SessionManager;

import java.util.Base64;

public class MainUserInterface extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_user_interface, container, false);
		TextView mainUserInterfaceHeading = view.findViewById(R.id.main_user_interface_heading);
		mainUserInterfaceHeading.setSelected(true);
		LinearLayout bloodPressure = view.findViewById(R.id.blood_pressure);
		bloodPressure.setOnClickListener(event -> {
			getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new BloodPressureScreen()).commit();
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout heartRate = view.findViewById(R.id.heart_rate);
		heartRate.setOnClickListener(event -> {
			getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new HeartRateScreen()).commit();
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout events = view.findViewById(R.id.event);
		events.setOnClickListener(event -> {
			Intent intent = new Intent(requireActivity(), EventActivityScreen.class);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout alarm = view.findViewById(R.id.alarm);
		alarm.setOnClickListener(event -> {
			Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout pedometer = view.findViewById(R.id.pedometer);
		pedometer.setOnClickListener(event -> {
			Intent intent = new Intent(requireActivity(), Pedometer.class);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout reportGeneration = view.findViewById(R.id.report);
		reportGeneration.setOnClickListener(event -> {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
				String updatedUrlUid = Base64.getEncoder()
				 .encodeToString(SessionManager.getUserId().getBytes());

				Log.d("TAG", "onCreateView: " + updatedUrlUid);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fitness-studio-v0.netlify.app/analysis/" + updatedUrlUid));
				startActivity(intent);
			} else {
				Toast.makeText(getContext(), "Version Error", Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout subscribeButton = view.findViewById(R.id.subscribe);
		subscribeButton.setOnClickListener(event -> {
			getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new SubscriptionFragment()).commit();
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		return view;
	}
}