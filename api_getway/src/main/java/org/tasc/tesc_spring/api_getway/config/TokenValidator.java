package org.tasc.tesc_spring.api_getway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    @Value("${application.security.jwt.public_key}")
    public  String public_key ;

    private PublicKey getPublicKey(String key) {

        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate private key", e);
        }

    }

        public Claims isValidToken(String token) {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getPublicKey(public_key)) // Đảm bảo sử dụng khóa công khai đúng.
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return  claims;
    }
}