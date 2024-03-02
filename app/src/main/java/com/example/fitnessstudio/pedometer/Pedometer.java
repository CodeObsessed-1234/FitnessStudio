package com.example.fitnessstudio.pedometer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;


public class Pedometer extends AppCompatActivity {
	private int targetStepsUser = 0;
	private float stepLengthInMeters = 0.70f;
	private TextView pedometerAnswer;
	private ProgressBar stepProgressbar;
	private TextView pedometerAnswerDistance;
	private AppCompatButton startTrackingButtonPedometer;
	private SessionManager sessionManager;


	@RequiresApi(api = Build.VERSION_CODES.Q)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionManager = new SessionManager(this);
		sessionManager.addStepReset(false);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (ContextCompat.checkSelfPermission(Pedometer.this,
		 android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
			requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 19);
		}
		setContentView(R.layout.activity_pedometer);

		EditText targetStepsField = findViewById(R.id.pedometerTargetSteps);
		targetStepsField.requestFocus();


		startTrackingButtonPedometer = findViewById(R.id.start_tracking_button_pedometer);


		if (ContextCompat.checkSelfPermission(Pedometer.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
		}

		Intent intent = getIntent();

		AppCompatButton resultButton = findViewById(R.id.result_pedometer);
		resultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setFragment(new PedometerResult());
				resultButton.setVisibility(View.GONE);
			}
		});
		startTrackingButtonPedometer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isMyServiceRunning(MyServicePedometer.class)) {
					stopService(new Intent(Pedometer.this, MyServicePedometer.class));
				}

				sessionManager.addStepReset(false);
				if (!targetStepsField.getText().toString().equals("")) {
					targetStepsUser = Integer.parseInt(targetStepsField.getText().toString());
				}

				sessionManager.addTargetSteps(targetStepsUser);
				startService(new Intent(Pedometer.this, MyServicePedometer.class));

				finish();
			}


		});

	}

	private void setFragment(PedometerResult fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.frame_layout_pedometer_result, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {

		super.onResume();


	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}