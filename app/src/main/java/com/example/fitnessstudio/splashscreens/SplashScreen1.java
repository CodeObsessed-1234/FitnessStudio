package com.example.fitnessstudio.splashscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessstudio.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen1);
        TextView textView=this.findViewById(R.id.skip_text_view_splash_screen1);
        textView.setOnClickListener(event->skipButtonClicked(this.findViewById(R.id.skip_text_view_splash_screen1)));
    }
    public void skipButtonClicked(View view){
        Toast.makeText(this,"Skip button pressed",Toast.LENGTH_SHORT).show();
    }
}