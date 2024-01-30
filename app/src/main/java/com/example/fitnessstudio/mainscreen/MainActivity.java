package com.example.fitnessstudio.mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.splashscreen.SplashScreen1;


public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Handler().postDelayed(() -> {
			Intent intent = new Intent(MainActivity.this, SplashScreen1.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			finish();
		},3000);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}
}