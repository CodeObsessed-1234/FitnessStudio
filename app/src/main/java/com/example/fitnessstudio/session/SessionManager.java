package com.example.fitnessstudio.session;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
	public static SharedPreferences sharedpreferences;
	public static final String MyPREFERENCE = "LoginSession";
	public static SharedPreferences.Editor editor;

	public SessionManager(Context context) {
		sharedpreferences = context.getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
	}

	public static void addLoginSession(boolean isLogin) {
		editor.putBoolean("Login", isLogin);
		editor.commit();
	}

	public void addStepReset(boolean reset) {
		editor.putBoolean("stepReset", reset);
		editor.commit();
	}

	public void subscriptionDuration(int duration){
		editor.putInt("subDuration",duration);
		editor.commit();
	}
	public int getSubscriptionDuration(){
		return sharedpreferences.getInt("subDuration",0);
	}

	public void addStepCount(int count) {
		editor.putInt("stepsCount", count);
		editor.commit();
	}

	public int getStepCount() {
		return sharedpreferences.getInt("stepsCount", 0);
	}

	public void addPresentStepCount(int count) {
		editor.putInt("presentCount", count);
		editor.commit();
	}

	public int getPresentStepCount() {
		return sharedpreferences.getInt("presentCount", 0);
	}

	public void addTargetSteps(int count) {
		editor.putInt("targetSteps", count);
		editor.commit();
	}

	public int getTargetSteps() {
		return sharedpreferences.getInt("targetSteps", 0);
	}

	public boolean isLoggedIn() {
		return sharedpreferences.getBoolean("Login", false);
	}

	public void addUserId(String Uid){
		editor.putString("uid",Uid);
		editor.commit();
	}
	public static void removeUser(){
		editor.putBoolean("isRemove",true);
		editor.commit();
	}
	public static boolean isRemove(){
		return sharedpreferences.getBoolean("isRemove",false);
	}
	public static String getUserId(){
		return sharedpreferences.getString("uid",null);
	}
}
