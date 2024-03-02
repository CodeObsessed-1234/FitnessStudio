package com.example.fitnessstudio.pedometer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;

import java.util.Locale;


public class PedometerResult extends Fragment {

	private ProgressBar stepProgressbar;
	private TextView pedometerAnswer;
	private TextView pedometerAnswerDistance;
	private SessionManager sessionManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_pedometer_result, container, false);
		sessionManager = new SessionManager(getContext());
		stepProgressbar = view.findViewById(R.id.pedometerProgressBar);
		pedometerAnswer = view.findViewById(R.id.pedometerAnswer);
		pedometerAnswerDistance = view.findViewById(R.id.pedometerAnswerDistance);

		stepProgressbar.setMax(sessionManager.getTargetSteps());
		stepProgressbar.setProgress(sessionManager.getStepCount());
		pedometerAnswer.setText("Steps walked..." + sessionManager.getPresentStepCount() + " / " + sessionManager.getTargetSteps());

		float distanceInKm = (float) ((sessionManager.getStepCount() * 0.2) / 1000);
		pedometerAnswerDistance.setText(String.format(Locale.getDefault(), "Distance: %2f KM", distanceInKm));
		return view;
	}
}