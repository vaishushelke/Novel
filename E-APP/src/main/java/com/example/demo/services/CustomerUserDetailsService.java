package com.example.demo.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repos.UserRepo;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	 private UserRepo userRepo;

	    @Override
	    public UserDetails loadUserByUsername(String username)
	            throws UsernameNotFoundException {

	        User user = userRepo.findByUsername(username)
	                .orElseThrow(() ->
	                    new UsernameNotFoundException("User not found"));

	        return new org.springframework.security.core.userdetails.User(
	                user.getUsername(),
	                user.getPassword(),
	                Collections.singleton(
	                    new SimpleGrantedAuthority(
	                        "ROLE_" + user.getRole().name()
	                    )
	                )
	        );
	    }

}
