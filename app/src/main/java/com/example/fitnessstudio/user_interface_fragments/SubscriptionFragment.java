package com.example.fitnessstudio.user_interface_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.subscription.SubscriptionModel;
import com.example.fitnessstudio.subscription.SubscriptionRecyclerViewAdapter;
import com.example.fitnessstudio.user_interface.UserInterface;

import java.util.ArrayList;
import java.util.Objects;

public class SubscriptionFragment extends Fragment {
	RecyclerView recyclerView;

	ArrayList<SubscriptionModel> subscriptionModels = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_subscription, container, false);
		recyclerView = view.findViewById(R.id.subscription_recyclerview);


		ArrayList<String> subscriptionId = ((UserInterface) requireActivity()).getSubscriptionIdList();
		ArrayList<String> subscriptionName = ((UserInterface) requireActivity()).getSubscriptionNameList();
		ArrayList<String> subscriptionDuration = ((UserInterface) requireActivity()).getSubscriptionDurationList();
		ArrayList<String> subscriptionPrice = ((UserInterface) requireActivity()).getSubscriptionPriceList();
		if (subscriptionId.isEmpty()) {
			Toast.makeText(getContext(), "Network Problem", Toast.LENGTH_SHORT).show();
			FragmentManager fragmentManager = getParentFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.frame_layout_user_interface, new MainUserInterface());
			fragmentTransaction.commit();
		}
		for (int i = 0; i < Objects.requireNonNull(subscriptionId).size(); i++) {
			assert subscriptionName != null;
			assert subscriptionDuration != null;
			assert subscriptionPrice != null;

			Toast.makeText(getContext(), subscriptionName.get(i), Toast.LENGTH_SHORT).show();

			subscriptionModels.add(new SubscriptionModel(subscriptionId.get(i), subscriptionName.get(i), subscriptionDuration.get(i), subscriptionPrice.get(i)));
		}
		SubscriptionRecyclerViewAdapter subscriptionRecyclerViewAdapter = new SubscriptionRecyclerViewAdapter(getContext(), subscriptionModels);
		recyclerView.setAdapter(subscriptionRecyclerViewAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				setEnabled(false);
				FragmentManager fragmentManager = getParentFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.frame_layout_user_interface, new MainUserInterface());
				fragmentTransaction.commit();
			}
		});
		return view;
	}

}