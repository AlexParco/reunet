package com.reunet.app.security.jwt;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    @Value("${reunet.app.token.secret_key}")
    private String JWT_KEY;
    @Value("${reunet.app.token.expiration}")
    private int JWT_EXPIRATION;

    public String generateToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, JWT_KEY)
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
