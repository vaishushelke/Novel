package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.services.CustomerUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    // Authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService);

        provider.setPasswordEncoder(
                passwordEncoder());

        return provider;
    }

    // Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Public
                .requestMatchers(
                        "/auth/reg",
                        "/auth/verify-otp",
                        "/auth/login",
                        "/novel/all",              
                        "/novel/{id}",
                        "/novel/category",
                        "/novel/author",
                        "/novel/title"
                        
                ).permitAll()

                // ADMIN only
                .requestMatchers("/novel/**")
                .hasRole("ADMIN")
                
                .requestMatchers("/cart/**")
                .hasRole("USER")
                
                .requestMatchers("/order/**")
                .hasRole("USER")

                // USER or ADMIN
                .requestMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Everything else
                .anyRequest()
                .authenticated()
            )

            .authenticationProvider(
                    authenticationProvider()
            )

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
	
}
