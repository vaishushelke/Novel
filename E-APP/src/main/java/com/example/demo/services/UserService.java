package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repos.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepository;
}
