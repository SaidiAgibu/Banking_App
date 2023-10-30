package com.saidi.banking_app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {
    private static final long JWT_EXPIRATION = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date expiryDate = new Date(JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return token;
    }

    @Deprecated
    public String getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }

    @Deprecated
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", e.fillInStackTrace());
        }
    }
}
