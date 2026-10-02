package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Novel;
import com.example.demo.entities.Order1;
import com.example.demo.entities.User;
import com.example.demo.repos.CartItemRepo;
import com.example.demo.repos.CartRepo;
import com.example.demo.repos.NovelRepo;
import com.example.demo.repos.OrderRepo;
import com.example.demo.repos.UserRepo;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class OrderService {
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

	
	  

	
	public List<Order1> getMyOrders(String username) {

	    User user = userRepo.findByUsername(username)
	            .orElseThrow(() ->
	                new RuntimeException("User not found"));

	    return orderRepo.findByUserOrderByIdDesc(user);
	}



	public void deleteItemId(int id) {
		// TODO Auto-generated method stub
		
	}


	 private final String razorpayKey = 
			 "rzp_test_Td60a8w1JwA1Zp";
	 private final String razorpaySecret = 
			 "OYj7dG3DLzDgMPbRstKGfnit";
	
	   public Map<String, Object> createRazorpayOrder(String username) throws RazorpayException {
		// TODO Auto-generated method stub
		   
		   // 1. Find logged-in user
	        User user = userRepo.findByUsername(username)
	                .orElseThrow(() ->
	                        new RuntimeException("User not found"));


	        // 2. Find user's CART
	        Cart cart = cartRepo.findByUserId(user.getId())
	                .orElseThrow(() ->
	                        new RuntimeException("Cart not found"));


	        // 3. Check cart items
	        if (cart.getItem() == null ||
	            cart.getItem().isEmpty()) {

	            throw new RuntimeException("Cart is empty");
	        }


	        // 4. Calculate total
	        double totalAmount = 0;

	        for (CartItem item : cart.getItem()) {

	            Novel novel = item.getNovel();

	            // Check novel
	            if (novel == null) {

	                throw new RuntimeException(
	                        "Novel not found in cart"
	                );
	            }


	            // Check quantity
	            if (item.getQuentity() <= 0) {

	                throw new RuntimeException(
	                        "Invalid quantity"
	                );
	            }


	            // Check stock
	            if (novel.getStock() < item.getQuentity()) {

	                throw new RuntimeException(
	                        novel.getTitle()
	                        + " does not have enough stock"
	                );
	            }


	            // Calculate item price
	            totalAmount +=
	                    novel.getPrice()
	                    * item.getQuentity();
	        }


	        // 5. Convert rupees to paise
	        int amount =
	                (int) Math.round(totalAmount * 100);


	        // 6. Create Razorpay client
	        RazorpayClient razorpayClient =
	                new RazorpayClient(
	                        razorpayKey,
	                        razorpaySecret
	                );


	        // 7. Create Razorpay request
	        JSONObject orderRequest =
	                new JSONObject();

	        orderRequest.put(
	                "amount",
	                amount
	        );

	        orderRequest.put(
	                "currency",
	                "INR"
	        );

	        orderRequest.put(
	                "receipt",
	                "novelnest_"
	                + System.currentTimeMillis()
	        );

	        orderRequest.put(
	                "payment_capture",
	                1
	        );


	        // 8. Create Razorpay order
	        com.razorpay.Order razorpayOrder =
	                razorpayClient.orders.create(
	                        orderRequest
	                );


	        // 9. Send response to React
	        Map<String, Object> response =
	                new HashMap<>();

	        response.put(
	                "id",
	                razorpayOrder.get("id")
	        );

	        response.put(
	                "amount",
	                razorpayOrder.get("amount")
	        );

	        response.put(
	                "currency",
	                razorpayOrder.get("currency")
	        );

	        // Only Key ID goes to frontend
	        response.put(
	                "key",
	                razorpayKey
	        );


	        return response;
	}



	public List<Order1> getOrdersByUser(Integer userId) {
		// TODO Auto-generated method stub
		return orderRepo.findOrderByUserId(userId);
	}
}
