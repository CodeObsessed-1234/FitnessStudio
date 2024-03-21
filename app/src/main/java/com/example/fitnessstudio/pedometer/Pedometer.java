package com.example.fitnessstudio.pedometer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;


public class Pedometer extends AppCompatActivity {
	private int targetStepsUser = 0;
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


		AppCompatButton startTrackingButtonPedometer = findViewById(R.id.start_tracking_button_pedometer);


		AppCompatButton resultButton = findViewById(R.id.result_pedometer);
		resultButton.setOnClickListener(v -> {
			setFragment(new PedometerResult());
			resultButton.setVisibility(View.GONE);
		});
		startTrackingButtonPedometer.setOnClickListener(v -> {
			if (isMyServiceRunning(MyServicePedometer.class)) {
				stopService(new Intent(Pedometer.this, MyServicePedometer.class));
			}

			sessionManager.addStepReset(false);
			if (!targetStepsField.getText().toString().isEmpty()) {
				targetStepsUser = Integer.parseInt(targetStepsField.getText().toString());
			}

			sessionManager.addTargetSteps(targetStepsUser);
			startService(new Intent(Pedometer.this, MyServicePedometer.class));

			finish();
		});

	}

	private void setFragment(PedometerResult fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.frame_layout_pedometer_result, fragment);
		fragmentTransaction.commit();
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