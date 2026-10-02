package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Cart;
import com.example.demo.entities.User;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {
	
	Optional<Cart> findByUserId(int userId);

	Optional<Cart> findByUser(User user);

}
