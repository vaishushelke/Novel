package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Novel;
import com.example.demo.entities.Order1;
import com.example.demo.entities.User;
import com.example.demo.repos.NovelRepo;
import com.example.demo.repos.OrderRepo;
import com.example.demo.repos.UserRepo;

@Service
public class AdminService {

	private final NovelRepo novelRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

  public AdminService(
            NovelRepo novelRepository,
            UserRepo userRepository,
            OrderRepo orderRepository) {

        this.novelRepo = novelRepository;
        this.userRepo = userRepository;
        this.orderRepo = orderRepository;
    }

    // ================= DASHBOARD =================

    public Map<String, Object> getDashboard() {

        List<Novel> books = novelRepo.findAll();
        List<User> users = userRepo.findAll();
        List<Order1> orders = orderRepo.findAll();

        double revenue = orders.stream()
                .filter(order -> order.getTotalAmount() > 0)
                .mapToDouble(Order1::getTotalAmount)
                .sum();

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalBooks", books.size());
        dashboard.put("totalUsers", users.size());
        dashboard.put("totalOrders", orders.size());
        dashboard.put("totalRevenue", revenue);

        return dashboard;
    }

    // ================= BOOKS =================

    public List<Novel> getAllBooks() {
        return novelRepo.findAll();
    }

    public Novel addBook(Novel novel) {
        return novelRepo.save(novel);
    }

    public Novel updateBook(Integer id, Novel novel) {

        Novel existingBook = novelRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id: " + id));

        existingBook.setTitle(novel.getTitle());
        existingBook.setDescription(novel.getDescription());
        existingBook.setPrice(novel.getPrice());
        existingBook.setStock(novel.getStock());
        existingBook.setImageUrl(novel.getImageUrl());
        existingBook.setAuthor(novel.getAuthor());
        existingBook.setCategory(novel.getCategory());

        return novelRepo.save(existingBook);
    }

    public void deleteBook(Integer id) {

        if (!novelRepo.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }

        novelRepo.deleteById(id);
    }

    // ================= USERS =================

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Integer id) {

        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepo.deleteById(id);
    }

    // ================= ORDERS =================

    public List<Order1> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order1 updateOrderStatus(Integer id, String status) {

        Order1 order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);

        return orderRepo.save(order);
    }
	
}
