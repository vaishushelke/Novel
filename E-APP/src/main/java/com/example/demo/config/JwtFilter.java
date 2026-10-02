package com.example.demo.config;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.services.CustomerUserDetailsService;
import com.example.demo.services.JwtService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	 private JwtService jwtService;

	    @Autowired
	    private CustomerUserDetailsService userDetailsService;
	    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		  String path = request.getServletPath();

		    // Public endpoints - no JWT required
		    if (path.equals("/auth/reg") ||
		        path.equals("/auth/verify-otp") ||
		        path.equals("/auth/login")) {

		        filterChain.doFilter(request, response);
		        return;
		    }
		 String authHeader =
	                request.getHeader("Authorization");

	        String token = null;
	        String username = null;

	        // 1. Check Authorization header
	        if (authHeader != null &&
	                authHeader.startsWith("Bearer ")) {

	            token = authHeader.substring(7);

	            username =
	                    jwtService.extractUsername(token);
	        }

	        // 2. Check user and SecurityContext
	        if (username != null &&
	                SecurityContextHolder
	                    .getContext()
	                    .getAuthentication() == null) {

	            UserDetails userDetails =
	                    userDetailsService
	                        .loadUserByUsername(username);

	            // 3. Validate JWT
	            if (jwtService.validateToken(
	                    token,
	                    userDetails)) {

	                UsernamePasswordAuthenticationToken
	                        authentication =
	                        new UsernamePasswordAuthenticationToken(
	                                userDetails,
	                                null,
	                                userDetails.getAuthorities()
	                        );

	                authentication.setDetails(
	                        new WebAuthenticationDetailsSource()
	                                .buildDetails(request)
	                );

	                // 4. Store authentication
	                SecurityContextHolder
	                        .getContext()
	                        .setAuthentication(authentication);
	            }
	        }

	        // 5. Continue filter chain
	        filterChain.doFilter(request, response);
		
	}
}
