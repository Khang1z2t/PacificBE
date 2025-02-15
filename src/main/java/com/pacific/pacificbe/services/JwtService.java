package com.pacific.pacificbe.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUserName(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimmsResolver);

    String generateToken(Map<String, Object> extraClaims);

    public String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

}
