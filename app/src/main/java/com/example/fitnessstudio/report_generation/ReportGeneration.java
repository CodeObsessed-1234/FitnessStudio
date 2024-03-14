package com.example.fitnessstudio.report_generation;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.blood_pressure.BloodPressureActivity;

public class ReportGeneration extends AppCompatActivity {
    public static boolean isReport=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this, BloodPressureActivity.class);
        startActivity(intent);
        finish();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_generation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}