package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Order1;
import com.example.demo.entities.User;

@Repository
public interface OrderRepo extends JpaRepository<Order1,Integer> {

	List<Order1> findByUserOrderByIdDesc(User user);

	List<Order1> findOrderByUserId(Integer userId);

}
