package com.example.fitnessstudio.splash_screen;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessstudio.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Handler().postDelayed(()->
                        getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_main_activity,new SplashScreen3()).commit()
                ,3000L);
        requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        return inflater.inflate(R.layout.fragment_splash_screen2, container, false);
    }
}