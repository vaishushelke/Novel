package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;

public interface UserRepo extends JpaRepository<User,Integer>{
	
	Optional<User> findByUsername(String Username);
	
	boolean existsByUsername(String username);

}
