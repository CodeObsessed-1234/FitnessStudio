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

	public  static void addLoginSession(boolean isLogin) {
		editor.putBoolean("Login", isLogin);
		editor.commit();
	}

	public  boolean isLoggedIn() {
		return sharedpreferences.getBoolean("Login", false);
	}

	public void logoutSession() {
		editor.clear();
		editor.commit();
	}
}
