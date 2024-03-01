package com.example.fitnessstudio.splash_screen;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends Fragment {
    protected static boolean moveNext=true;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(requireActivity());
        if(sessionManager.isLoggedIn())
            moveNext=false;
        requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }
}