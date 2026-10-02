package com.example.demo.entities;

public class OtpRequest {

	private String otp;
	private String username;
	private String mesage;
	public OtpRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OtpRequest(String otp, String username, String mesage) {
		super();
		this.otp = otp;
		this.username = username;
		this.mesage = mesage;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMesage() {
		return mesage;
	}
	public void setMesage(String mesage) {
		this.mesage = mesage;
	}
	@Override
	public String toString() {
		return "OtpRequest [otp=" + otp + ", username=" + username + ", mesage=" + mesage + "]";
	}
	
}
