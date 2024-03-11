package com.example.fitnessstudio.login_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;
import com.example.fitnessstudio.user_data.UserData;
import com.example.fitnessstudio.user_interface.UserInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen3 extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen3);
		Intent intent = getIntent();
		String phoneNumber = intent.getStringExtra("mobileNumber");
		String uid = intent.getStringExtra("uid");
		EditText name = this.findViewById(R.id.edit_text_name_login_screen3);
		EditText email = this.findViewById(R.id.edit_text_email_login_screen3);
		AppCompatButton continueButton = this.findViewById(R.id.continue_button_login_screen3);
		continueButton.setOnClickListener(event -> {
			SessionManager.addLoginSession(true);
			String enteredName = name.getText().toString();
			String emailEntered = email.getText().toString();
			if (TextUtils.isEmpty(enteredName) || TextUtils.isEmpty(emailEntered))
				Toast.makeText(this, "Enter valid details.", Toast.LENGTH_SHORT).show();
			else {
				addDataInDatabase(uid, enteredName, phoneNumber, emailEntered);
				Intent intentNext = new Intent(this, UserInterface.class);
				startActivity(intentNext);
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}

	private void addDataInDatabase(String uid, String name, String phoneNumber, String email) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("Users").child(uid);
		Map<String, Object> heartRateMap = new HashMap<>();
		Map<String, Object> bloodPressureMap = new HashMap<>();
		Map<String, Object> pedometerMap = new HashMap<>();
		UserData userData = new UserData(Calendar.getInstance().get(Calendar.MONTH) + 1 + "-" + Calendar.getInstance().get(Calendar.YEAR), name, email, phoneNumber,heartRateMap,bloodPressureMap,pedometerMap);
		SessionManager sessionManager = new SessionManager(this);
		sessionManager.addUserId(uid);
		usersReference.setValue(userData);
	}
}