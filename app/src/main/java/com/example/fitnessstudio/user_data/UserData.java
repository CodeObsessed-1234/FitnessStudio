package com.example.fitnessstudio.user_data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class UserData {
	public String createTime;
	public String UserName;
	public String Email;
	public String PhoneNumber;

	public UserData(String createTime, String userName, String email, String phoneNumber) {
		this.createTime = createTime;
		UserName = userName;
		Email = email;
		PhoneNumber = phoneNumber;
	}
}
