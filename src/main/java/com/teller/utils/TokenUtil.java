package com.teller.utils;

import com.teller.constant.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.apache.kafka.common.security.JaasUtils.SERVICE_NAME;

@Component
public class TokenUtil {
    private static final long EXPIRATION_TIME = 1000 * 60 * 10;
    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateToken(String crmId) {
        return Jwts.builder()
                .setSubject(crmId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String validateToken(String token) throws CommonException {
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
            throw new CommonException(ResponseCode.TOKEN_EXPIRE.getCode(), ResponseCode.TOKEN_EXPIRE.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }
}
