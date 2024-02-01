package com.example.fitnessstudio.main_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.splash_screen.SplashScreen;
import com.example.fitnessstudio.splash_screen.SplashScreen1;


public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fragmentManager=getSupportFragmentManager();
		FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.frame_layout_main_activity,new SplashScreen());
		fragmentTransaction.commit();
		new Handler().postDelayed(()->
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main_activity,new SplashScreen1()).commit()
		,3000L);
	}
}