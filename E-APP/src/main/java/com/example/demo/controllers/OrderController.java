package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Novel;
import com.example.demo.entities.Order1;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.User;
import com.example.demo.helpers.ApiResponse;
import com.example.demo.repos.CartItemRepo;
import com.example.demo.repos.CartRepo;
import com.example.demo.repos.NovelRepo;
import com.example.demo.repos.OrderRepo;
import com.example.demo.repos.UserRepo;
import com.example.demo.services.OrderService;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:5173")
public class OrderController {
	
	@Autowired
	   OrderService oser;
	   

	    @Autowired
	    private UserRepo userRepo;
	    
	    @Autowired
	    private CartRepo cartRepo;
	    
	    @Autowired
	    private CartItemRepo cartItemRepo;
	    
	    @Autowired
	    private NovelRepo novelRepo;
	    
	    @Autowired
	    private OrderRepo orderRepo;

	    @PostMapping("/create-order")
	    public ResponseEntity<?> createOrder(
	            Authentication authentication) {

	        try {

	            String username = authentication.getName();

	            Map<String, Object> response =
	                    oser.createRazorpayOrder(username);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Map.of(
	                            "success", false,
	                            "message", e.getMessage()
	                    ));
	        }
	    }
	 
	    
	    @PostMapping("/place-order")
	    public ResponseEntity<?> placeOrder(
	            @RequestBody Map<String, Object> data,
	            Authentication authentication) {

	        try {

	            String username = authentication.getName();

	            User user = userRepo.findByUsername(username)
	                    .orElseThrow(() ->
	                            new RuntimeException("User not found"));

	            Cart cart = cartRepo.findByUserId(user.getId())
	                    .orElseThrow(() ->
	                            new RuntimeException("Cart not found"));

	            if (cart.getItem() == null ||
	                cart.getItem().isEmpty()) {

	                return ResponseEntity.badRequest().body(
	                        Map.of(
	                                "success", false,
	                                "message", "Cart is empty"
	                        )
	                );
	            }


	            double totalAmount = 0;

	            // Create Order
	            Order1 order = new Order1();

	            order.setUser(user);
	            order.setStatus("PAID");


	            order.setPhone(
	                    (String) data.get("phone"));

	            order.setAddress(
	                    (String) data.get("address"));

	            order.setCity(
	                    (String) data.get("city"));

	            order.setState(
	                    (String) data.get("state"));

	            order.setPincode(
	                    (String) data.get("pincode"));


	            List<OrderItem> orderItems =
	                    new ArrayList<>();


	            // Cart → OrderItems
	            for (CartItem cartItem : cart.getItem()) {

	                Novel novel = cartItem.getNovel();

	                if (novel.getStock() < cartItem.getQuentity()) {

	                    return ResponseEntity.badRequest().body(
	                            Map.of(
	                                    "success", false,
	                                    "message",
	                                    "Insufficient stock for "
	                                    + novel.getTitle()
	                            )
	                    );
	                }


	                double itemTotal =
	                        novel.getPrice() *
	                        cartItem.getQuentity();

	                totalAmount += itemTotal;


	                OrderItem orderItem =
	                        new OrderItem();

	                orderItem.setNovel(novel);

	                orderItem.setQuantity(
	                        cartItem.getQuentity());

	                orderItem.setPrice(
	                        novel.getPrice());

	                orderItem.setOrder(order);

	                orderItems.add(orderItem);


	                // Reduce stock
	                novel.setStock(
	                        novel.getStock()
	                        - cartItem.getQuentity());

	                novelRepo.save(novel);
	            }


	            order.setTotalAmount(totalAmount);
	            order.setItems(orderItems);


	            // Save Order1
	            Order1 savedOrder =
	                    orderRepo.save(order);


	            // =================================
	            // DELETE CART ITEMS
	            // =================================

	            cart.getItem().clear();
	            

	            cartRepo.save(cart);


	            return ResponseEntity.ok(
	                    Map.of(
	                            "success", true,
	                            "message", "Order placed successfully",
	                            "order", savedOrder
	                    )
	            );
	            


	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(
	                            Map.of(
	                                    "success", false,
	                                    "message", e.getMessage()
	                            )
	                    );
	        }
	    }

	  
		 @DeleteMapping("/deleteItem/{id}")
		 public Map<String,String> deleteItem(@PathVariable int id){

		     oser.deleteItemId(id);

		     return Map.of("message","Item removed");
		 }
	  

	@GetMapping("/my-orders")
	public ResponseEntity<?> getMyOrders(
	        Authentication authentication) {

	    try {

	        String username =
	            authentication.getName();

	        List<Order1> orders =
	            oser.getMyOrders(username);

	        return ResponseEntity.ok(orders);

	    } catch (RuntimeException e) {

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(e.getMessage());
	    }
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getOrdersByUser(
	        @PathVariable Integer userId) {

	    List<Order1> orders =
	            oser.getOrdersByUser(userId);

	    return ResponseEntity.ok(
	            new ApiResponse(
	                    true,
	                    "Orders fetched successfully",
	                    orders
	            )
	    );
	}

}