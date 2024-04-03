package com.example.fitnessstudio.user_interface_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.event.EventActivityScreen;
import com.example.fitnessstudio.pedometer.Pedometer;
import com.example.fitnessstudio.session.SessionManager;
import com.example.fitnessstudio.user_interface.UserInterface;

import java.util.Base64;

public class MainUserInterface extends Fragment {

	private static final int REQUEST_CODE_PICK_FILE = 1001;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_user_interface, container, false);
		TextView mainUserInterfaceHeading = view.findViewById(R.id.main_user_interface_heading);
		mainUserInterfaceHeading.setSelected(true);
		LinearLayout bloodPressure = view.findViewById(R.id.blood_pressure);
		bloodPressure.setOnClickListener(event -> {
			if (((UserInterface) requireActivity()).isSubscribed()) {
				getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new BloodPressureScreen()).commit();
				requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			} else Toast.makeText(getContext(), "Not Subscribed!", Toast.LENGTH_SHORT).show();
		});
		LinearLayout heartRate = view.findViewById(R.id.heart_rate);
		heartRate.setOnClickListener(event -> {
			if (((UserInterface) requireActivity()).isSubscribed()) {
				getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new HeartRateScreen()).commit();
				requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			} else Toast.makeText(getContext(), "Not Subscribed!", Toast.LENGTH_SHORT).show();

		});
		LinearLayout events = view.findViewById(R.id.event);
		events.setOnClickListener(event -> {
			if (((UserInterface) requireActivity()).isSubscribed()) {
				Intent intent = new Intent(requireActivity(), EventActivityScreen.class);
				startActivity(intent);
				requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

			} else Toast.makeText(getContext(), "Not Subscribed!", Toast.LENGTH_SHORT).show();

		});
		LinearLayout alarm = view.findViewById(R.id.alarm);
		alarm.setOnClickListener(event -> {
			Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout pedometer = view.findViewById(R.id.pedometer);
		pedometer.setOnClickListener(event -> {
			Intent intent = new Intent(requireActivity(), Pedometer.class);
			startActivity(intent);
			requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
		LinearLayout reportGeneration = view.findViewById(R.id.report);
		reportGeneration.setOnClickListener(event -> {

			if (((UserInterface) requireActivity()).isSubscribed()) {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
					String updatedUrlUid = Base64.getEncoder()
					 .encodeToString(SessionManager.getUserId().getBytes());
					Log.d("TAG", "onCreateView: " + updatedUrlUid);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fitness-studio-v01.netlify.app/analysis/" + updatedUrlUid));
					startActivity(intent);
				} else {
					Toast.makeText(getContext(), "Version Error", Toast.LENGTH_SHORT).show();
				}
			}
			else Toast.makeText(getContext(), "Not Subscribed!", Toast.LENGTH_SHORT).show();
		});
		LinearLayout share = view.findViewById(R.id.share);
		share.setOnClickListener(event -> {
			if (((UserInterface) requireActivity()).isSubscribed()) {
				if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
				}
				startFilePicker();
			}
			else Toast.makeText(getContext(), "Not Subscribed!", Toast.LENGTH_SHORT).show();
		});
		LinearLayout subscribeButton = view.findViewById(R.id.subscribe);
		subscribeButton.setOnClickListener(event -> {
			if (((UserInterface) requireActivity()).isSubscribed()) {
				Toast.makeText(getContext(), "Already Subscribed!", Toast.LENGTH_SHORT).show();
			} else {
				getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_user_interface, new SubscriptionFragment()).commit();
				requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		return view;
	}

	private void startFilePicker() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("application/pdf"); // Specific MIME type for PDF files
		startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedFileUri = data.getData();
			shareFile(selectedFileUri);
		}
	}

	private void shareFile(Uri fileUri) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("application/pdf"); // Set the MIME type of the file
		shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
		startActivity(Intent.createChooser(shareIntent, "Share File"));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 100) {
			// Check if the permission is granted
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				startFilePicker();
			} else {
				Toast.makeText(getContext(), "pdf will not be able to share", Toast.LENGTH_SHORT).show();
			}
		}
	}
}