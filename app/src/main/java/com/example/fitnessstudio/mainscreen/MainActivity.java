package com.example.fitnessstudio.mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.splashscreens.SplashScreen1;


public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Handler().postDelayed(() -> {
//					connection screen ka beech ka kam --------------------
			Intent intent = new Intent(MainActivity.this, SplashScreen1.class);
			startActivity(intent);
			finish();
		},3000);
	}
}