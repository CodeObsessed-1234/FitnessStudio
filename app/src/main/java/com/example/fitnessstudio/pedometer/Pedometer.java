package com.example.fitnessstudio.pedometer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;

import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class Pedometer extends AppCompatActivity implements SensorEventListener {
	private int targetStepsUser = 100;

	private SensorManager sensorManager;
	private Sensor stepCounterSensor;
	private int notificationID = 1001;
	private String channelId = "10001";
	private long stepCount = 0;
	private float stepLengthInMeters = 0.70f;
	private long startTime;
	private TextView pedometerAnswer;
	private ProgressBar stepProgressbar;
	private TextView pedometerAnswerDistance;
	private AppCompatButton startTrackingButtonPedometer;
	private AppCompatButton stopTrackingButtonStartTrackingButtonPedometer;


	@RequiresApi(api = Build.VERSION_CODES.Q)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (ContextCompat.checkSelfPermission(Pedometer.this,
		 android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
			requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 19);
		}
		setContentView(R.layout.activity_pedometer);

		Toast.makeText(Pedometer.this, "ff", Toast.LENGTH_SHORT).show();
		EditText targetStepsField = findViewById(R.id.pedometerTargetSteps);
		targetStepsField.requestFocus();

		stepProgressbar = findViewById(R.id.pedometerProgressBar);
		pedometerAnswer = findViewById(R.id.pedometerAnswer);
		pedometerAnswerDistance = findViewById(R.id.pedometerAnswerDistance);
		startTrackingButtonPedometer = findViewById(R.id.start_tracking_button_pedometer);
		stopTrackingButtonStartTrackingButtonPedometer = findViewById(R.id.stop_tracking_button_start_tracking_button_pedometer);


		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


		if (stepCounterSensor == null) {
			pedometerAnswer.setText("StepCount Sensor is not Present");
		}
		startTrackingButtonPedometer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(Pedometer.this, MyServicePedometer.class);
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//					ContextCompat.startForegroundService(Pedometer.this, intent);
//				} else {
//					startService(intent);
//				}

				if (stepCounterSensor != null) {
					if (!targetStepsField.getText().toString().equals("")) {
						targetStepsUser = Integer.parseInt(targetStepsField.getText().toString());
					} else {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pedometer.this);
						alertDialog.setMessage("Enter target steps otherwise 0 is default value");
						alertDialog.setCancelable(false);
						alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

								startTime = System.currentTimeMillis();
								startTrackingButtonPedometer.setVisibility(View.GONE);
								stopTrackingButtonStartTrackingButtonPedometer.setVisibility(View.VISIBLE);
								stepProgressbar.setVisibility(View.VISIBLE);
								pedometerAnswerDistance.setVisibility(View.VISIBLE);
								GifImageView gifImageView = findViewById(R.id.pedometerAnim);
								gifImageView.setVisibility(View.GONE);
								Toast.makeText(Pedometer.this, "" + stepCount, Toast.LENGTH_SHORT).show();
							}
						});
						alertDialog.show();
						alertDialog.create();
					}

				}
			}

		});
		stopTrackingButtonStartTrackingButtonPedometer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				SessionManager.addStepReset(true);
//				SessionManager.addStepCount(stepCount);
				stopTrackingButtonStartTrackingButtonPedometer.setVisibility(View.GONE);
				startTrackingButtonPedometer.setVisibility(View.VISIBLE);
				Toast.makeText(Pedometer.this, "stop" + stepCount, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (stepCounterSensor != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	protected void onResume() {

		super.onResume();
		sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);

		stepProgressbar.setMax(targetStepsUser);
		stepCount = 0;
		Toast.makeText(this, "yapp", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//			if (SessionManager.getStepReset()) {
//				stepCount = (long) event.values[0] - SessionManager.getStepCount();
//			} else {
//				stepCount = (long) event.values[0];
//			}

			stepCount = (long) event.values[0];
			Toast.makeText(this, "change" + stepCount, Toast.LENGTH_SHORT).show();
			pedometerAnswer.setText("The Step Walked... " + stepCount);
			stepProgressbar.setProgress((int) stepCount);
			if (stepCount >= targetStepsUser) {
				pedometerAnswer.setText("TargetAchived :) You Walked... " + stepCount + " steps");
			}
			float distanceInKm = stepCount * stepLengthInMeters / 100;
			pedometerAnswerDistance.setText(String.format(Locale.getDefault(), "Distance: %2f KM", distanceInKm));
		}
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}