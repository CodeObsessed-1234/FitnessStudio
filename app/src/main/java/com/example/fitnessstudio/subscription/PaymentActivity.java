package com.example.fitnessstudio.subscription;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.ArrayList;
import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {
	EditText name, note;
	TextView amount;
	private static String payerName, payerNote, payerAmount;
	final int UPI_PAYMENT = 0;


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
		note = findViewById(R.id.transactionNote);
		amount = findViewById(R.id.transactionAmount);
		AppCompatButton pay = findViewById(R.id.transactionPay);
		AppCompatButton cancel = findViewById(R.id.transactionCancel);

		name.requestFocus();

		Intent intent = getIntent();
		amount.setText(intent.getStringExtra("amount") + "₹");
		subscriptionId = intent.getStringExtra("subId");
		subscriptionDuration = intent.getStringExtra("subDuration");

		pay.setOnClickListener(event -> {
			payerName = name.getText().toString();
			payerNote = note.getText().toString();
			payerAmount = amount.getText().toString();

			if (!payerName.isEmpty() && !payerNote.isEmpty() && !payerAmount.isEmpty()) {
				payUsingUpi(payerAmount, "8470808950@apl", "8470808950@apl");
			} else {
				Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			}
		});

		cancel.setOnClickListener(v -> finish());
	}


	void payUsingUpi(String amount, String upiId, String name) {

		Uri uri = Uri.parse("upi://pay").buildUpon()
		 .appendQueryParameter("pa", upiId)
		 .appendQueryParameter("pn", upiId)
		 .appendQueryParameter("mc", "1234")
		 .appendQueryParameter("tid", "123456")
		 .appendQueryParameter("tr", "asgdfuyas3153")
		 .appendQueryParameter("am", amount)
		 .appendQueryParameter("mam", amount)
		 .appendQueryParameter("cu", "INR")
		 .build();


		Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
		upiPayIntent.setData(uri);

		// will always show a dialog to user to choose an app
		Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

		// check if intent resolves
		if (null != chooser.resolveActivity(getPackageManager())) {
			startActivityForResult(chooser, UPI_PAYMENT);
		} else {
			Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == UPI_PAYMENT) {
			if ((RESULT_OK == resultCode) || (resultCode == 11)) {
				if (data != null) {
					String trxt = data.getStringExtra("response");
					ArrayList<String> dataList = new ArrayList<>();
					dataList.add(trxt);
					upiPaymentDataOperation(dataList);
				} else {
					ArrayList<String> dataList = new ArrayList<>();
					dataList.add("nothing");
					upiPaymentDataOperation(dataList);
				}
			} else {
				ArrayList<String> dataList = new ArrayList<>();
				dataList.add("nothing");
				upiPaymentDataOperation(dataList);
			}
		}
	}

	private void upiPaymentDataOperation(ArrayList<String> data) {
		if (isConnectionAvailable(this)) {
			String str = data.get(0);
			String paymentCancel = "";
			if (str == null) str = "discard";
			String status = "";
			String approvalRefNo = "";
			String response[] = str.split("&");
			for (int i = 0; i < response.length; i++) {
				String equalStr[] = response[i].split("=");
				if (equalStr.length >= 2) {
					if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
						status = equalStr[1].toLowerCase();
					} else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
						approvalRefNo = equalStr[1];
					}
				} else {
					paymentCancel = "Payment cancelled by user.";
				}
			}

			if (status.equals("success")) {
				Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
				FirebaseDatabase database = FirebaseDatabase.getInstance();
				DatabaseReference userReference = database.getReference("Users").child(SessionManager.getUserId());
				String dateFormat = Calendar.getInstance().get(Calendar.DATE) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
				DatabaseReference userSubscriptionReference = userReference.child("subscribed");
				userSubscriptionReference.child(dateFormat).setValue(subscriptionId);
				SessionManager sessionManager = new SessionManager(this);
				sessionManager.subscriptionDuration(Integer.parseInt(subscriptionDuration));
				finish();
			} else if ("Payment cancelled by user.".equals(paymentCancel)) {
				Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
		}
	}

	public static boolean isConnectionAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			return netInfo != null && netInfo.isConnected()
			 && netInfo.isConnectedOrConnecting()
			 && netInfo.isAvailable();
		}
		return false;
	}
}