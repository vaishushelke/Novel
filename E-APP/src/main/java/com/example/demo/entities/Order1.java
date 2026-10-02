package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Order1 {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double totalAmount;

    private String status;
    
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;

    // User ↔️ Order
    @ManyToOne
    @JsonIgnore
    private User user;

    // Order ↔️ OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> items = new ArrayList<>();

	public Order1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order1(Integer id, double totalAmount, String status, String phone, String address, String city,
			String state, String pincode, User user, List<OrderItem> items) {
		super();
		this.id = id;
		this.totalAmount = totalAmount;
		this.status = status;
		this.phone = phone;
		this.address = address;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.user = user;
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Order1 [id=" + id + ", totalAmount=" + totalAmount + ", status=" + status + ", phone=" + phone
				+ ", address=" + address + ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", user="
				+ user + ", items=" + items + "]";
	}
	
    
}
