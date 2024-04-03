package com.example.fitnessstudio.subscription;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {
	EditText name, upi_id, note;
	TextView amount;
	private static String payerName, upiId, payerNote, payerAmount, status;
	Uri uri;

	public static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
	String subscriptionId, subscriptionDuration;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_payment);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});
		name = findViewById(R.id.transactionUserName);
		upi_id = findViewById(R.id.transactionUPIID);
		note = findViewById(R.id.transactionNote);
		amount = findViewById(R.id.transactionAmount);
		AppCompatButton pay = findViewById(R.id.transactionPay);
		AppCompatButton cancel = findViewById(R.id.transactionCancel);

		name.requestFocus();

		Intent intent = getIntent();
		amount.setText(intent.getStringExtra("amount") + "$");
		subscriptionId = intent.getStringExtra("subId");
		subscriptionDuration = intent.getStringExtra("subDuration");

		pay.setOnClickListener(event -> {
			payerName = name.getText().toString();
			upiId = upi_id.getText().toString();
			payerNote = note.getText().toString();
			payerAmount = amount.getText().toString();
			if (!payerName.isEmpty() && !upiId.isEmpty() && !payerNote.isEmpty() && !payerAmount.isEmpty()) {
				uri = getUpiPaymentUri(payerName, upiId, payerNote, payerAmount);
				payWithGpay();
			} else {
				Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			}
		});

		cancel.setOnClickListener(v -> finish());
	}

	private static Uri getUpiPaymentUri(String name, String upiId, String note, String amount) {
		return new Uri.Builder()
		 .scheme("upi")
		 .authority("pay")
		 .appendQueryParameter("pa", upiId)
		 .appendQueryParameter("pn", name)
		 .appendQueryParameter("tn", note)
		 .appendQueryParameter("am", amount)
		 .appendQueryParameter("cu", "INR")
		 .build();
	}

	private void payWithGpay() {
		if (isAppInstalled(this, PaymentActivity.GPAY_PACKAGE_NAME)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(uri);
			intent.setPackage(PaymentActivity.GPAY_PACKAGE_NAME);
			startActivityForResult(intent, 0);
		} else {
			Toast.makeText(this, "Google Pay is not Installed. Please try install GPAY and then try again", Toast.LENGTH_SHORT).show();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			status = Objects.requireNonNull(data.getStringExtra("status")).toLowerCase();
		}
		if ((RESULT_OK == resultCode) && Objects.equals(status, "success")) {
			Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference userReference = database.getReference("Users").child(SessionManager.getUserId());
			String dateFormat = Calendar.getInstance().get(Calendar.DATE) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
			DatabaseReference userSubscriptionReference = userReference.child("subscribed");
			userSubscriptionReference.child(dateFormat).setValue(subscriptionId);
			SessionManager sessionManager = new SessionManager(this);
			sessionManager.subscriptionDuration(Integer.parseInt(subscriptionDuration));
			finish();
		}

	}

	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}