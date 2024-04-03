package com.example.fitnessstudio.subscription;

public class SubscriptionModel {
	String subscriptionId;
	String subscriptionName;
	String subscriptionDuration;
	String SubscriptionPrice;

	public SubscriptionModel(String subscriptionId, String subscriptionName, String subscriptionDuration, String subscriptionPrice) {
		this.subscriptionId = subscriptionId;
		this.subscriptionName = subscriptionName;
		this.subscriptionDuration = subscriptionDuration;
		SubscriptionPrice = subscriptionPrice;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public String getSubscriptionName() {
		return subscriptionName;
	}

	public String getSubscriptionDuration() {
		return subscriptionDuration;
	}

	public String getSubscriptionPrice() {
		return SubscriptionPrice;
	}
}
