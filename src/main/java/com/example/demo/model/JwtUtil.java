package com.example.demo.model;

import com.example.demo.request.UserDetailsResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private SecretKey key;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        // Convert the secret string into a SecretKey
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate a token with user details, expiring in 1 hour
    public String generateToken(UserDetailsResponse userDetails) {
        Map<String, Object> claims = convertUserDetailsToMap(userDetails);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                .signWith(key, SignatureAlgorithm.HS512) // Use the SecretKey object here
                .compact();
    }

    // Convert UserDetailsResponse to a Map for storing in JWT claims
    private Map<String, Object> convertUserDetailsToMap(UserDetailsResponse userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("username", userDetails.getUsername());
        claims.put("email", userDetails.getEmail());
        claims.put("cccd", userDetails.getCccd());
        claims.put("gender", userDetails.getGender());
        claims.put("dateOfBirth", userDetails.getDateOfBirth());
        claims.put("bloodGroup", userDetails.getBloodGroup());
        claims.put("address", userDetails.getHomeAddress());
        return claims;
    }

 // Extract UserDetailsResponse from token
    public UserDetailsResponse extractUserDetails(String token) {
        Claims claims = extractAllClaims(token);
        return new UserDetailsResponse(
                new BigInteger(claims.get("id").toString()),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.get("cccd", String.class),
                claims.get("gender", String.class),
                claims.get("dateOfBirth", String.class),
                claims.get("bloodGroup", String.class),
                claims.get("address", String.class) // Thêm address
        );
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Validate the token
    public Boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    // Extract email from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
