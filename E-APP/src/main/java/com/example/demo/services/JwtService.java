package com.example.demo.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	
	private final String SECRET =
            "mySecretKeyForJwtAuthenticationApplication123456789";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role",
                        userDetails.getAuthorities()
                                .iterator()
                                .next()
                                .getAuthority())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 30)
                )
                .signWith(key)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // Validate token
    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Check expiration
    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Extract claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
