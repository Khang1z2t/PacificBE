package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.services.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Override
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String userId = ((User) userDetails).getId();
        extraClaims.put("firstName", ((User) userDetails).getFirstName());
        extraClaims.put("lastName", ((User) userDetails).getLastName());
        extraClaims.put("username", userDetails.getUsername());
        extraClaims.put("email", ((User) userDetails).getEmail());
        extraClaims.put("role", ((User) userDetails).getRole());
        extraClaims.put("status", ((User) userDetails).getStatus());
        extraClaims.put("version", "2.0");

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", Header.JWT_TYPE);
        header.put("kid", "pacific-tni-2025");

        return Jwts
                .builder()
                .setHeader(header)
                .setClaims(extraClaims)
                .setSubject(userId) // Sử dụng userId thay vì username
                .setIssuer("Pacific TNI")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        // So sánh userId thay vì username
        return (userId.equals(((User) userDetails).getId())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
