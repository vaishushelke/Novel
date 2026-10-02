package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private int quentity;
	
	@ManyToOne
	@JoinColumn(name="cart_id")
	private Cart cart;
	
	@ManyToOne
	@JoinColumn(name="novel_id")
	private Novel novel;

	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItem(Integer id, int quentity, Cart cart, Novel novel) {
		super();
		this.id = id;
		this.quentity = quentity;
		this.cart = cart;
		this.novel = novel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuentity() {
		return quentity;
	}

	public void setQuentity(int quentity) {
		this.quentity = quentity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Novel getNovel() {
		return novel;
	}

	public void setNovel(Novel novel) {
		this.novel = novel;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", quentity=" + quentity + ", cart=" + cart + "]";
	}

	
	
	
	
	
	
}
