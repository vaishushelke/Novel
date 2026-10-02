package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.OtpRequest;
import com.example.demo.entities.User;
import com.example.demo.repos.UserRepo;
import com.example.demo.services.CustomerUserDetailsService;
import com.example.demo.services.EmailService;
import com.example.demo.services.JwtService;

@RestController
@RequestMapping("auth")
@CrossOrigin("http://localhost:5173")
public class AuthController {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;


    // REGISTER
    @PostMapping("/reg")
    public ResponseEntity<?> register(
            @RequestBody User user) {

        // Generate 6 digit OTP
        String otp = String.valueOf(
                (int)(Math.random() * 900000) + 100000
        );

        // Encode password
        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        // Save OTP
        user.setOtp(otp);

        // OTP valid for 5 minutes
        user.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        // User is not verified yet
        user.setVerified(false);

        userRepo.save(user);//database

        // Send OTP
        emailService.sendOtp(
                user.getUsername(),
                otp
        );

        return ResponseEntity.ok(
                Map.of(
                    "message",
                    "Registration successful. OTP sent to your email.",
                    "username",
                    user.getUsername()
                )
        );
    }


    // VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody OtpRequest request) {

        User user = userRepo //database user
                .findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "User not found"
                    ));
        }

        if (user.getOtp() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "OTP not found"
                    ));
        }

        if (LocalDateTime.now()
                .isAfter(user.getOtpExpiry())) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "OTP expired"
                    ));
        }

        if (!user.getOtp() //database otp
                .equals(request.getOtp())) {//fe otp

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "Invalid OTP"
                    ));
        }

        // OTP verified
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepo.save(user);

        return ResponseEntity.ok(
                Map.of(
                    "message",
                    "OTP verified successfully. You can now login."
                )
        );
    }


    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody User user) {

        User dbUser = userRepo
                .findByUsername(user.getUsername())
                .orElse(null);

        if (dbUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "User not found"
                    ));
        }

        if (!dbUser.isVerified()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "message",
                        "Please verify your OTP first"
                    ));
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService
                    .loadUserByUsername(
                        user.getUsername()
                    );

        String token =
                jwtService.generateToken(
                        userDetails
                );

        return ResponseEntity.ok(
                Map.of(
                    "message",
                    "Login successful",
                    "token",
                    token
                )
        );
    }
	
	
	
	

}
