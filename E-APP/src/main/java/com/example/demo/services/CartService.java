package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Novel;
import com.example.demo.entities.User;
import com.example.demo.repos.CartItemRepo;
import com.example.demo.repos.CartRepo;
import com.example.demo.repos.NovelRepo;
import com.example.demo.repos.UserRepo;

@Service
public class CartService {

	@Autowired
    private UserRepo userRepo;

	     @Autowired
	    private CartRepo cartRepo;

	    @Autowired
	    private CartItemRepo cartItemRepo;
	   

	    @Autowired
	    private NovelRepo novelRepo;
	    


	    // Get User Cart
	    public Cart getCart(int userId) {

	        return cartRepo
	                .findByUserId(userId)
	                .orElse(null);
	    }


		
		public CartItem addToCart(String username, int novelId) {

	        // 1. Find user using JWT username/email
	        User user = userRepo.findByUsername(username)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        // 2. Find novel
	        Novel novel = novelRepo.findById(novelId)
	                .orElseThrow(() -> new RuntimeException("Novel not found"));

	        // 3. Find user's cart
	        Cart cart = cartRepo.findByUser(user)
	                .orElseGet(() -> {

	                    Cart newCart = new Cart();
	                    newCart.setUser(user);

	                    return cartRepo.save(newCart);
	                });

	        // 4. Check whether novel already exists in cart
	        Optional<CartItem> existingItem =
	                cartItemRepo.findByCartAndNovel(cart, novel);

	        CartItem cartItem = new CartItem();
	        if (existingItem.isPresent()) {

	            // 5. Increase quantity
	             cartItem = existingItem.get();

	            cartItem.setQuentity(cartItem.getQuentity() + 1);

	            cartItemRepo.save(cartItem);

	        } else {

	            // 6. Create new cart item
	             cartItem = new CartItem();

	            cartItem.setCart(cart);
	            cartItem.setNovel(novel);
	            cartItem.setQuentity(1);

	            cartItemRepo.save(cartItem);
	        }
	        novelRepo.save(novel);
	        return cartItem;
	    }

		
		 // Update Quantity
	    public CartItem updateQuantity(
	            int id,
	            int quantity) {

	        CartItem item =
	                cartItemRepo.findById(id).orElse(null);

	        if (item == null) {
	            return null;
	        }

	        item.setQuentity(quantity);

	        return cartItemRepo.save(item);
	    }


	    // Remove Item
	    public boolean removeItem(int id) {

	        if (!cartItemRepo.existsById(id)) {
	            return false;
	        }

	        cartItemRepo.deleteById(id);

	        return true;
	    }


	    // Clear Cart
//	    public void clearCart(int cartId) {
//
//	        cartItemRepo.deleteByCartId(cartId);
//	    }
	    
	 // Clear Cart
	    public void clearCart(int cartId) {

		    Cart cart = cartRepo.findById(cartId)
		            .orElseThrow(() -> new RuntimeException("Cart not found"));

		    cart.getItem().clear();

		    cartRepo.save(cart);
		}


		public List<CartItem> getCartItems(int cartId) {
			// TODO Auto-generated method stub
	        return cartItemRepo
	                .findByCartId(cartId);
		}


		
		public List<CartItem> getCart(String username) {

		    User user = userRepo.findByUsername(username)
		            .orElseThrow(() -> new RuntimeException("User not found"));

		    Cart cart = cartRepo.findByUser(user)
		            .orElseThrow(() -> new RuntimeException("Cart not found"));

		    return cart.getItem();

		    
		}


			public String increaseQuantity(Integer cartItemId, String username) {

			    User user = userRepo.findByUsername(username)
			            .orElseThrow(() -> new RuntimeException("User not found"));

			    Cart cart = cartRepo.findByUser(user)
			            .orElseThrow(() -> new RuntimeException("Cart not found"));

			    CartItem cartItem = cartItemRepo.findById(cartItemId)
			            .orElseThrow(() -> new RuntimeException("Cart item not found"));

			    // Make sure this cart item belongs to logged-in user's cart
			    if (!cartItem.getCart().getId().equals(cart.getId())) {
			        throw new RuntimeException("Unauthorized access");
			    }

			    cartItem.setQuentity(cartItem.getQuentity() + 1);

			    cartItemRepo.save(cartItem);

			    return "Quantity increased";
			}


				public String decreaseQuantity(Integer cartItemId, String username) {

				    User user = userRepo.findByUsername(username)
				            .orElseThrow(() -> new RuntimeException("User not found"));

				    Cart cart = cartRepo.findByUser(user)
				            .orElseThrow(() -> new RuntimeException("Cart not found"));

				    CartItem cartItem = cartItemRepo.findById(cartItemId)
				            .orElseThrow(() -> new RuntimeException("Cart item not found"));

				    if (!cartItem.getCart().getId().equals(cart.getId())) {
				        throw new RuntimeException("Unauthorized access");
				    }

				    if (cartItem.getQuentity() > 1) {

				        cartItem.setQuentity(cartItem.getQuentity() - 1);
				        cartItemRepo.save(cartItem);

				    } else {

				        cartItemRepo.delete(cartItem);
				    }

				    return "Quantity decreased";
			   }

}
