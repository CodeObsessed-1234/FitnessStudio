package com.example.fitnessstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginScreen1 extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen1);
		String[] countryCode = getResources().getStringArray(R.array.country_code);

		Spinner countryCodeDropbox = findViewById(R.id.country_code_dropbox);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,countryCode);
		countryCodeDropbox.setAdapter(adapter);
		countryCodeDropbox.setEnabled(true);

		TextView countryCodeText = findViewById(R.id.country_code_text);

		countryCodeDropbox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				countryCodeText.setText(countryCode[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
}