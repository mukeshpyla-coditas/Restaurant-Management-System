package com.mukesh.restaurantManagementSystem.util;

import com.mukesh.restaurantManagementSystem.service.implementations.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey;
    private CustomUserDetailsService userDetailsService;

    JwtUtil(
            @Value("${spring.jwt.secret-key}")
            String secretKey,
            CustomUserDetailsService userDetailsService
    ) {
        this.secretKey = secretKey;
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .claims()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 12)))
                .and()
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractUsername(token));
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
