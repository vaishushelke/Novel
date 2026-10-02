package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@NotBlank(message="can not be blank")
	private String username;
	@NotBlank(message="can not be blank")
	@Pattern(
			regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
			message="Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character"
			)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Role role;
	
	 private String otp;
	 
	 private LocalDateTime otpExpiry;
	 
	 private boolean verified=false;
	 
	 @OneToOne(mappedBy="user" , cascade=CascadeType.ALL)
	 @JsonIgnore
	 private Cart cart;
	 
	 @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	 @JsonIgnore
	 private List<Order1> orders=new ArrayList<>();

	 public User() {
		super();
		// TODO Auto-generated constructor stub
	 }

	 public User(Integer id, @NotBlank(message = "can not be blank") String username,
			@NotBlank(message = "can not be blank") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character") String password,
			Role role, String otp, LocalDateTime otpExpiry, boolean verified, Cart cart, List<Order1> orders) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.otp = otp;
		this.otpExpiry = otpExpiry;
		this.verified = verified;
		this.cart = cart;
		this.orders = orders;
	 }

	 public Integer getId() {
		 return id;
	 }

	 public void setId(Integer id) {
		 this.id = id;
	 }

	 public String getUsername() {
		 return username;
	 }

	 public void setUsername(String username) {
		 this.username = username;
	 }

	 public String getPassword() {
		 return password;
	 }

	 public void setPassword(String password) {
		 this.password = password;
	 }

	 public Role getRole() {
		 return role;
	 }

	 public void setRole(Role role) {
		 this.role = role;
	 }

	 public String getOtp() {
		 return otp;
	 }

	 public void setOtp(String otp) {
		 this.otp = otp;
	 }

	 public LocalDateTime getOtpExpiry() {
		 return otpExpiry;
	 }

	 public void setOtpExpiry(LocalDateTime otpExpiry) {
		 this.otpExpiry = otpExpiry;
	 }

	 public boolean isVerified() {
		 return verified;
	 }

	 public void setVerified(boolean verified) {
		 this.verified = verified;
	 }

	 public Cart getCart() {
		 return cart;
	 }

	 public void setCart(Cart cart) {
		 this.cart = cart;
	 }

	 public List<Order1> getOrders() {
		 return orders;
	 }

	 public void setOrders(List<Order1> orders) {
		 this.orders = orders;
	 }

	 @Override
	 public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", otp="
				+ otp + ", otpExpiry=" + otpExpiry + ", verified=" + verified + ", cart=" + cart + ", orders=" + orders
				+ "]";
	 }
	 
	 
	
}
