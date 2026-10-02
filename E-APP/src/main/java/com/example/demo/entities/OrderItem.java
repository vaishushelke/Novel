package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int quantity;

    private double price;

    // Order ↔ OrderItem
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order1 order;

    // Novel ↔ OrderItem
    @ManyToOne
    @JoinColumn(name = "novel_id")
    private Novel novel;

	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderItem(Integer id, int quantity, double price, Order1 order, Novel novel) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
		this.novel = novel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Order1 getOrder() {
		return order;
	}

	public void setOrder(Order1 order) {
		this.order = order;
	}

	public Novel getNovel() {
		return novel;
	}

	public void setNovel(Novel novel) {
		this.novel = novel;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", quantity=" + quantity + ", price=" + price + ", order=" + order + ", novel="
				+ novel + "]";
	}
    
    
	
}
