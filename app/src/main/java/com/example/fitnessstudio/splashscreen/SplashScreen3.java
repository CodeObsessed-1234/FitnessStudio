package com.example.fitnessstudio.splashscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessstudio.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen3);
        TextView skipTextView=this.findViewById(R.id.skip_text_view_splash_screen3);
        skipTextView.setOnClickListener(event-> Toast.makeText(this,"You pressed skip button 3",Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}