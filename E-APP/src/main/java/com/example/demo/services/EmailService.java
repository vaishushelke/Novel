package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String sender;

	public void sendOtp(String username, String otp) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setFrom(sender);
	    message.setTo(username);
	    message.setSubject("OTP Verification");

	    message.setText(
	        "Your OTP is: " + otp +
	        "\nThis OTP is valid for 5 minutes."
	    );

	    mailSender.send(message);
}
}
