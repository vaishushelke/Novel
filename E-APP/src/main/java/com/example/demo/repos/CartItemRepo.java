package com.example.demo.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Novel;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {

	List<CartItem> findByCartId(int cartId);

    Optional<CartItem> findByCartIdAndNovelId(int cartId, int novelId);

    void deleteByCartId(int cartId);

	Optional<CartItem> findByCartAndNovel(Cart cart, Novel novel);
	
	

}
