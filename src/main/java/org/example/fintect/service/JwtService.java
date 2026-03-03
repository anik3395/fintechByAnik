package org.example.fintect.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.fintect.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSecretKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        return  Keys.hmacShaKeyFor(encodedKey);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 *1000))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private  boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean isValidToken(String token , User user) {
        if (!isTokenExpired(token)  && extractUsername(token).equals(user.getEmail())) {
            return true;
        }else  {
            return false;
        }
    }

    private String extractRole(String token) {
        return getClaimsFromToken(token).get("role").toString();
    }

}
