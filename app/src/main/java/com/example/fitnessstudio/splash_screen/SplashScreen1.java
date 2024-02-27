package com.example.fitnessstudio.splash_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.user_interface.UserInterface;

@SuppressLint("CustomSplashScreen")
public class SplashScreen1 extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!SplashScreen.moveNext){
            Intent intent = new Intent(requireActivity(), UserInterface.class);
            startActivity(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else{
            new Handler().postDelayed(()->
                            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_main_activity,new SplashScreen2()).commit()
                    ,3000L);
            requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        return inflater.inflate(R.layout.fragment_splash_screen1, container, false);
    }
}