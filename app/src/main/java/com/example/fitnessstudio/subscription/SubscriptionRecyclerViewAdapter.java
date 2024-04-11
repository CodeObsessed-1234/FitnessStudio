package com.example.fitnessstudio.subscription;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessstudio.R;

import java.util.ArrayList;

public class SubscriptionRecyclerViewAdapter extends RecyclerView.Adapter<SubscriptionRecyclerViewAdapter.MyViewHolder> {

	private final ArrayList<SubscriptionModel> subscriptionModels;
	private final LayoutInflater mInflater;
	Context context;

	public SubscriptionRecyclerViewAdapter(Context context, ArrayList<SubscriptionModel> subscriptionModels) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.subscriptionModels = subscriptionModels;
	}

	@NonNull
	@Override
	public SubscriptionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.subscription_view, parent, false);
		return new MyViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull SubscriptionRecyclerViewAdapter.MyViewHolder holder, int position) {
		holder.subscriptionName.setText(subscriptionModels.get(position).getSubscriptionName());
		holder.subscriptionDuration.setText(subscriptionModels.get(position).getSubscriptionDuration() + "Yrs");
		holder.subscriptionPrice.setText(subscriptionModels.get(position).getSubscriptionPrice() + "₹");
		holder.subscriptionBuyButton.setOnClickListener(event -> {
			Intent intent = new Intent(context, PaymentActivity.class);
			intent.putExtra("amount", subscriptionModels.get(position).getSubscriptionPrice());
			intent.putExtra("subId", subscriptionModels.get(position).getSubscriptionId());
			intent.putExtra("subDuration",subscriptionModels.get(position).getSubscriptionDuration());
			context.startActivity(intent);
		});
	}

	@Override
	public int getItemCount() {
		return subscriptionModels.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		TextView subscriptionName, subscriptionDuration, subscriptionPrice;
		AppCompatButton subscriptionBuyButton;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			subscriptionName = itemView.findViewById(R.id.subscription_name);
			subscriptionDuration = itemView.findViewById(R.id.subscription_duration);
			subscriptionPrice = itemView.findViewById(R.id.subscription_price);
			subscriptionBuyButton = itemView.findViewById(R.id.subscription_buy_button);
		}
	}
}
