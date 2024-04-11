package com.example.fitnessstudio.splash_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.login_screen.LoginScreen1;


@SuppressLint("CustomSplashScreen")
public class SplashScreen4 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash_screen4, container, false);
		AppCompatButton getStartedButton = view.findViewById(R.id.get_started_button_splash_screen4);
		getStartedButton.setOnClickListener(event -> {


			Intent intent = new Intent(getActivity(), LoginScreen1.class);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			requireActivity().finish();


		});
		return view;
	}
}