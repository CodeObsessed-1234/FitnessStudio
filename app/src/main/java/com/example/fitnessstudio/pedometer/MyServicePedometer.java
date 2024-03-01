package com.example.fitnessstudio.pedometer;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyServicePedometer extends Service {
	public MyServicePedometer() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();


		NotificationManager notificationManager;
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationChannel channel = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			channel = new NotificationChannel("Player", "Sync Service", NotificationManager.IMPORTANCE_HIGH);
			channel.enableLights(true);
			channel.enableVibration(true);
			channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
			channel.setDescription("Service Name");
			notificationManager.createNotificationChannel(channel);
		}


		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Player")
		 .setContentTitle("Music Player")
		 .setContentText("My Music")
		 .setAutoCancel(false)
		 .setOngoing(true);

		Notification notification = builder.build();
		notificationManager.notify(1, notification);

	}
}

