package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {

}
