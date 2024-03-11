package com.example.fitnessstudio.user_data;

import java.util.Map;

public class UserData {
	public String createTime;
	public String UserName;
	public String Email;
	public String PhoneNumber;
	Map<String, Object> heartRateMap;
	Map<String, Object> bloodPressureMap;
	Map<String, Object> pedometerMap ;

	public UserData(String createTime, String userName, String email, String phoneNumber, Map<String, Object> heartRateMap, Map<String, Object> bloodPressureMap, Map<String, Object> pedometerMap) {
		this.createTime = createTime;
		UserName = userName;
		Email = email;
		PhoneNumber = phoneNumber;
		this.heartRateMap = heartRateMap;
		this.bloodPressureMap = bloodPressureMap;
		this.pedometerMap = pedometerMap;
	}
}
