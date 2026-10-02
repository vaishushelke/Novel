package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Novel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String title;
	private String description;
	private double price;
	private int stock;
	private String imageUrl;
	
	private String author;
	
	private String category;
	
	@OneToMany(mappedBy="novel")
	@JsonIgnore
	private List<CartItem> cartItem=new ArrayList<>();
	
	@OneToMany(mappedBy="novel")
	@JsonIgnore
	private List<OrderItem> orderItems=new ArrayList<>();

	public Novel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Novel(Integer id, String title, String description, double price, int stock, String imageUrl, String author,
			String category, List<CartItem> cartItem, List<OrderItem> orderItems) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.imageUrl = imageUrl;
		this.author = author;
		this.category = category;
		this.cartItem = cartItem;
		this.orderItems = orderItems;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(List<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "Novel [id=" + id + ", title=" + title + ", description=" + description + ", price=" + price + ", stock="
				+ stock + ", imageUrl=" + imageUrl + ", author=" + author + ", category=" + category + ", cartItem="
				+ cartItem + "]";
	}
	
	
}
