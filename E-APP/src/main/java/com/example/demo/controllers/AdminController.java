package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Novel;
import com.example.demo.entities.Order1;
import com.example.demo.entities.User;
import com.example.demo.services.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins="http://localhost:5173")
public class AdminController {

	@Autowired
	  private AdminService adminService;
	
	   
	  @GetMapping("/dashboard")
	    public ResponseEntity<?> dashboard() {
	        return ResponseEntity.ok(adminService.getDashboard());
	    }

	    // ================= BOOKS =================

	    @GetMapping("/books")
	    public ResponseEntity<List<Novel>> getAllBooks() {
	        return ResponseEntity.ok(adminService.getAllBooks());
	    }

	    @PostMapping("/books")
	    public ResponseEntity<Novel> addBook(@RequestBody Novel novel) {
	        return ResponseEntity.ok(adminService.addBook(novel));
	    }

	    @PutMapping("/books/{id}")
	    public ResponseEntity<Novel> updateBook(
	            @PathVariable Integer id,
	            @RequestBody Novel novel) {

	        return ResponseEntity.ok(adminService.updateBook(id, novel));
	    }

	    @DeleteMapping("/books/{id}")
	    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {

	        adminService.deleteBook(id);

	        return ResponseEntity.ok("Book deleted successfully");
	    }

	    // ================= USERS =================

	    @GetMapping("/users")
	    public ResponseEntity<List<User>> getAllUsers() {
	        return ResponseEntity.ok(adminService.getAllUsers());
	    }

	    @DeleteMapping("/users/{id}")
	    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {

	        adminService.deleteUser(id);

	        return ResponseEntity.ok("User deleted successfully");
	    }

	    // ================= ORDERS =================

	    @GetMapping("/orders")
	    public ResponseEntity<List<Order1>> getAllOrders() {
	        return ResponseEntity.ok(adminService.getAllOrders());
	    }

	    @PutMapping("/orders/{id}/status")
	    public ResponseEntity<Order1> updateOrderStatus(
	            @PathVariable Integer id,
	            @RequestParam String status) {

	        return ResponseEntity.ok(
	                adminService.updateOrderStatus(id, status)
	        );
	    }
}
