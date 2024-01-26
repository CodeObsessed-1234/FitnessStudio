package com.example.fitnessstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button=this.findViewById(R.id.button);
		button.setOnClickListener(event-> Toast.makeText(this,"Hello from me",Toast.LENGTH_SHORT).show());
	}
}