package com.example.fitnessstudio.splashscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessstudio.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        ImageView arrowBackImageView=this.findViewById(R.id.arrow_back_splash_screen2);
        TextView skipTextView=this.findViewById(R.id.skip_text_view_splash_screen2);
        arrowBackImageView.setOnClickListener(event->arrowButtonClickedSplashScreen2(arrowBackImageView));
        skipTextView.setOnClickListener(event-> Toast.makeText(this,"You pressed skip button 2",Toast.LENGTH_SHORT).show());
    }
    public void arrowButtonClickedSplashScreen2(View view){
        Intent intent=new Intent(this,SplashScreen1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}