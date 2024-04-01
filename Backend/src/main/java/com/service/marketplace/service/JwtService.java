package com.service.marketplace.service;

import com.service.marketplace.persistence.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(User userDetails);

    String generateToken(Map<String, Object> extraClaims, User userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
