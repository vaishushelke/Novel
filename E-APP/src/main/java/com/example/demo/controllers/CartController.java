package com.example.demo.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.CartItem;
import com.example.demo.services.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin("http://localhost:5173")
public class CartController {

	@Autowired
	  private CartService cartService;

	  
	  @GetMapping("/getC")
	  public ResponseEntity<List<CartItem>> getCart(Authentication authentication) {

	      String username = authentication.getName();

	      List<CartItem> cartItems = cartService.getCart(username);

	      return ResponseEntity.ok(cartItems);
	  }
	  

     //aa to cart
	    @PostMapping("/add/{novelId}")
	    public ResponseEntity<String> addToCart(
	            @PathVariable int novelId,
	            Authentication authentication) {

	        String username = authentication.getName();

	        CartItem item =
	                cartService.addToCart(username, novelId);

	        if (item == null) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)
	                    .body("Unable to add item to cart");
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body("Item added to cart successfully");
	    }
	    
	  


	    @PutMapping("/increase/{cartItemId}")
	    public ResponseEntity<String> increaseQuantity(    
	            @PathVariable Integer cartItemId,
	            Authentication authentication) {

	        String username = authentication.getName();

	        String message =
	                cartService.increaseQuantity(cartItemId, username);

	        return ResponseEntity.ok(message);
	    }
	    
	    
	    @PutMapping("/decrease/{cartItemId}")
	    public ResponseEntity<String> decreaseQuantity(
	            @PathVariable Integer cartItemId,
	            Authentication authentication) {

	        String username = authentication.getName();

	        String message =
	                cartService.decreaseQuantity(cartItemId, username);

	        return ResponseEntity.ok(message);
	    }
	    
	    // Get Cart Items
	    @GetMapping("/{cartId}/items")
	    public ResponseEntity<?> getCartItems(
	            @PathVariable int cartId) {

	        List<CartItem> items =
	                cartService.getCartItems(cartId);

	        if (items.isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)
	                    .body("Cart is empty");
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(items);
	    }


	    // Update Quantity
	    @PutMapping("/item/{id}")
	    public ResponseEntity<String> updateQuantity(
	            @PathVariable int id,
	            @RequestParam int quantity) {

	        CartItem item =
	                cartService.updateQuantity(id, quantity);

	        if (item == null) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)
	                    .body("Cart item not found");
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body("Cart item quantity updated successfully");
	    }


	    // Remove Item
	    @DeleteMapping("/item/{id}")
	    public ResponseEntity<String> removeItem(
	            @PathVariable int id) {

	        boolean removed =
	                cartService.removeItem(id);

	        if (!removed) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)
	                    .body("Cart item not found");
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body("Item removed from cart successfully");
	    }


	    // Clear Cart
	 // Clear Cart
	    @PostMapping("/clear/{cartId}")
	    public ResponseEntity<String> clearCart(
	            @PathVariable int cartId) {

	        cartService.clearCart(cartId);

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body("Cart cleared successfully");
	    }
}
