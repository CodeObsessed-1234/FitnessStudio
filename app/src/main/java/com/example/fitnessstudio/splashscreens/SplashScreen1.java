package com.example.fitnessstudio.splashscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        textView.setOnClickListener(event->Toast.makeText(this,"you pressed Skip button 1",Toast.LENGTH_SHORT).show());
    }
    @Override
    protected void onResume() {
        super.onResume();
        moveToNextActivity();
    }
    private void moveToNextActivity(){
        long time=4990;
        moveToNextActivityTime(time);
    }
    private void moveToNextActivityTime(long time){
        new Handler().postDelayed(()->{
            Intent intent=new Intent(this,SplashScreen2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        },time);
    }
}