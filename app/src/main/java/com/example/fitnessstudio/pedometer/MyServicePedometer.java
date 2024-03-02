package com.example.fitnessstudio.pedometer;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;

import java.util.Locale;
import java.util.Map;

public class MyServicePedometer extends Service implements SensorEventListener {
	private SessionManager sessionManager;
	//	private MediaPlayer mediaPlayer;
	private final String ChannelId = "1001";
	private NotificationManager notificationManager;
	private NotificationCompat.Builder builder;
	private int stepCount = 0;

	private int count = 0;
	private Intent intent2;
	private SensorManager sensorManager;
	private Sensor stepCounterSensor;
	private String notificationContentText;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sessionManager = new SessionManager(this);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);


		intent2 = new Intent(getApplicationContext(), PedometerReceiver.class);

		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, intent2, PendingIntent.FLAG_IMMUTABLE);

		notificationContentText = stepCount+" steps.";
		builder = new NotificationCompat.Builder(this, ChannelId)
		 .setSmallIcon(R.drawable.fitness_studio_icon)
		 .setContentTitle("Start Walking :)")
		 .setContentText(notificationContentText)
		 .addAction(R.drawable.baseline_close_24, "STOP", pi)
		 .setPriority(NotificationCompat.PRIORITY_HIGH);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
				channel = new NotificationChannel(ChannelId, "name", importance);
			}
			channel.setDescription("description");
			builder.setOngoing(true);

			notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);

			notificationManager.notify(0, builder.build());
		}


		return START_REDELIVER_INTENT;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		notificationManager.cancel(0);
		sessionManager.addStepCount(stepCount);
		Toast.makeText(this, "destroyed" + sessionManager.getStepCount(), Toast.LENGTH_SHORT).show();
		sensorManager.unregisterListener(this);
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

			if (count < 1) sessionManager.addStepCount((int) event.values[0]);
			++count;
			stepCount = (int) Math.abs(event.values[0] - sessionManager.getStepCount());
			sessionManager.addPresentStepCount(stepCount);
			Toast.makeText(this, "for broadcast " + stepCount, Toast.LENGTH_SHORT).show();
			notificationManager.cancel(0);
			builder.setContentText(String.valueOf(stepCount));
			notificationManager.notify(0, builder.build());
			Toast.makeText(this, "change" + stepCount, Toast.LENGTH_SHORT).show();



		}
		Toast.makeText(this, "changed op service", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}

