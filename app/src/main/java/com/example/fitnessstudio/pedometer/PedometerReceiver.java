package com.example.fitnessstudio.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.fitnessstudio.session.SessionManager;

public class PedometerReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		context.stopService(new Intent(context, MyServicePedometer.class));
		SessionManager sessionManager = new SessionManager(context);
		int val = sessionManager.getPresentStepCount();
		Log.d("TAG", "onReceive: " + val);
		Toast.makeText(context, "c"+context, Toast.LENGTH_SHORT).show();

	}
}
