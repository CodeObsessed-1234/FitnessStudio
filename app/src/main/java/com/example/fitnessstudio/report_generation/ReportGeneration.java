package com.example.fitnessstudio.report_generation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitnessstudio.session.SessionManager;

public class ReportGeneration extends AppCompatActivity {
    public static boolean isReport=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fitness-studio-v0.netlify.app/analysis/"+ SessionManager.getUserId())));
    }
}