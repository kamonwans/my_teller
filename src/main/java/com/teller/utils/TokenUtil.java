package com.teller.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenUtil {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    @Value("${jwt.secret-key}")
    private String secretKey;


    public String generateToken(String crmId) {
        System.out.println("Generated Secret Key: " + generateSecretKey());
        return Jwts.builder()
                .setSubject(crmId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("Token has expired");
            }
            return claims.getSubject();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];  // 32 bytes = 256 bits
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);  // คีย์จะถูกเข้ารหัสในรูปแบบ Base64
    }
}
