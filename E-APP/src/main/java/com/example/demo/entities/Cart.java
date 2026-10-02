package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@OneToOne
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval=true)
	@JsonIgnore
	private List<CartItem> item=new ArrayList<>();

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cart(Integer id, User user, List<CartItem> item) {
		super();
		this.id = id;
		this.user = user;
		this.item = item;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItem> getItem() {
		return item;
	}

	public void setItem(List<CartItem> item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user + "]";
	}
	
	
}
