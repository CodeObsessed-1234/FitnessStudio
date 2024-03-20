package com.example.fitnessstudio.pedometer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;


public class PedometerResult extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_pedometer_result, container, false);
		SessionManager sessionManager = new SessionManager(requireContext());
		ProgressBar stepProgressbar = view.findViewById(R.id.pedometerProgressBar);
		TextView pedometerAnswer = view.findViewById(R.id.pedometerAnswer);
		TextView pedometerAnswerDistance = view.findViewById(R.id.pedometerAnswerDistance);

		stepProgressbar.setMax(sessionManager.getTargetSteps());
		stepProgressbar.setProgress(sessionManager.getPresentStepCount());
		pedometerAnswer.setText("Steps walked..." + sessionManager.getPresentStepCount() + " / " + sessionManager.getTargetSteps());

		float distanceInKm = (float) ((sessionManager.getStepCount() * 0.2) / 1000);
		pedometerAnswerDistance.setText(String.format(Locale.getDefault(), "Distance: %2f KM", distanceInKm));

		AppCompatButton addToReportButton = view.findViewById(R.id.addToReportPedometer);
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("Users").child(sessionManager.getUserId());

		addToReportButton.setOnClickListener(v -> {
			String dateFormat = Calendar.getInstance().get(Calendar.DATE) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
			DatabaseReference pedometerRef = usersReference.child("pedometerMap");
			pedometerRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					if (snapshot.exists()) {
						pedometerRef.child(dateFormat).setValue(sessionManager.getPresentStepCount());
					} else {
						pedometerRef.child(dateFormat).setValue(sessionManager.getPresentStepCount());
						Toast.makeText(getContext(), "child not found", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					Toast.makeText(getContext(), "data not added " + error, Toast.LENGTH_SHORT).show();
				}
			});
		});
		return view;
	}
}