package com.example.fitnessstudio.user_interface;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.session.SessionManager;
import com.example.fitnessstudio.user_interface_fragments.MainUserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class UserInterface extends AppCompatActivity {

	private NotificationManager notificationManager;
	private NotificationCompat.Builder builder;
	boolean isSubscribed;
	String subscriptionId, subscriptionName, subscriptionDuration, subscriptionPrice;
	ArrayList<String> subscriptionIdList = new ArrayList<>();
	ArrayList<String> subscriptionNameList = new ArrayList<>();
	ArrayList<String> subscriptionDurationList = new ArrayList<>();
	ArrayList<String> subscriptionPriceList = new ArrayList<>();

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public ArrayList<String> getSubscriptionIdList() {
		return subscriptionIdList;
	}

	public ArrayList<String> getSubscriptionNameList() {
		return subscriptionNameList;
	}

	public ArrayList<String> getSubscriptionDurationList() {
		return subscriptionDurationList;
	}

	public ArrayList<String> getSubscriptionPriceList() {
		return subscriptionPriceList;
	}

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_interface);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
		}
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference userSubscriptionReference = database.getReference("Users").child(SessionManager.getUserId()).child("subscribed");
		userSubscriptionReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				isSubscribed = snapshot.exists();
				if (!snapshot.exists()) {
					DatabaseReference subscriptionReference = database.getReference("Subscriptions");
					subscriptionReference.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {

							for (DataSnapshot childSnapshot : snapshot.getChildren()) {
								subscriptionId = childSnapshot.getKey();
								assert subscriptionId != null;
								DatabaseReference subscriptionDetailsReference = subscriptionReference.child(subscriptionId);
								subscriptionDetailsReference.addValueEventListener(new ValueEventListener() {
									@Override
									public void onDataChange(@NonNull DataSnapshot subSnapshot) {
										for (DataSnapshot subChildSnapshot : subSnapshot.getChildren()) {
											if (Objects.equals(subChildSnapshot.getKey(), "name"))
												subscriptionName = (String) subChildSnapshot.getValue();
											if (Objects.equals(subChildSnapshot.getKey(), "duration"))
												subscriptionDuration = (String) subChildSnapshot.getValue();
											if (Objects.equals(subChildSnapshot.getKey(), "price"))
												subscriptionPrice = (String) subChildSnapshot.getValue();
										}

										subscriptionIdList.add(subscriptionId);
										subscriptionNameList.add(subscriptionName);
										subscriptionDurationList.add(subscriptionDuration);
										subscriptionPriceList.add(subscriptionPrice);

									}

									@Override
									public void onCancelled(@NonNull DatabaseError error) {
										Toast.makeText(UserInterface.this, "Database Error Child", Toast.LENGTH_SHORT).show();
									}
								});

							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {
							Toast.makeText(UserInterface.this, "Database Error Parent", Toast.LENGTH_SHORT).show();
						}
					});

				} else {
					String buySubscriptionDate;
					for (DataSnapshot childSnapshot : snapshot.getChildren()) {
						Toast.makeText(UserInterface.this, childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
						buySubscriptionDate = childSnapshot.getKey();
						String[] data = Objects.requireNonNull(buySubscriptionDate).split("-");
						Calendar subDate = Calendar.getInstance();
						subDate.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
						Calendar presentDate = Calendar.getInstance();
						presentDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DATE));
						SessionManager sessionManager = new SessionManager(UserInterface.this);
						if (sessionManager.getSubscriptionDuration() > 0) {
							if (isAtLeastNUnitsApart(subDate, presentDate, sessionManager.getSubscriptionDuration(), Calendar.YEAR)) {

								String channelId = "1001";
								builder = new NotificationCompat.Builder(UserInterface.this, channelId)
								 .setSmallIcon(R.drawable.fitness_studio_icon)
								 .setContentTitle("Alert")
								 .setContentText("Subscription Expired")
								 .setPriority(NotificationCompat.PRIORITY_HIGH);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									int importance = NotificationManager.IMPORTANCE_HIGH;
									NotificationChannel channel = null;
									channel = new NotificationChannel(channelId, "name", importance);
									channel.setDescription("description");
									notificationManager = getSystemService(NotificationManager.class);
									notificationManager.createNotificationChannel(channel);
									notificationManager.notify(0, builder.build());
								}
								isSubscribed = false;
							}
						}
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Toast.makeText(UserInterface.this, "Network Error in fetching data", Toast.LENGTH_SHORT).show();
			}
		});

		setFragment(new MainUserInterface());
	}

	public static boolean isAtLeastNUnitsApart(Calendar cal1, Calendar cal2, int n, int unit) {
		// Calculate the difference in the specified units between the two dates
		int unitsApart = getDifferenceInUnits(cal1, cal2, unit);

		// Check if the difference is at least n units
		return unitsApart >= n;
	}

	public static int getDifferenceInUnits(Calendar cal1, Calendar cal2, int unit) {
		// Calculate the difference between the two dates in the specified units
		int diff = 0;
		while (cal1.before(cal2)) {
			cal1.add(unit, 1);
			diff++;
		}
		return diff;
	}


	private void setFragment(MainUserInterface fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.frame_layout_user_interface, fragment);
		fragmentTransaction.commit();
	}
}