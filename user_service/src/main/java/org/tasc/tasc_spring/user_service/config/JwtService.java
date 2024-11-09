package org.tasc.tasc_spring.user_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.public_key}")
    public  String public_key ;

    @Value("${application.security.jwt.private-key}")
    public  String private_key ;

    @Value("${application.security.jwt.expiration}")
    private Long expiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;

    public String generateToken(UserDetails userDetails)  {
        return  generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        extraClaims.put("full_Name",userDetails.getUsername());
        return buildToken(extraClaims, userDetails, expiration);
    }
    private PrivateKey getPrivateKey(String key)  {

        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate private key", e);
        }
    }
    private PublicKey getPublicKey(String key)  {

        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec (keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate private key", e);
        }
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration)  {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getPrivateKey(private_key))
                .compact();
    }
    public String generateRefreshToken(
            UserDetails userDetails
    )  {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public Claims extractToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey(public_key)) // Đảm bảo sử dụng khóa công khai đúng.
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey(public_key))
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public String extractUsername(String token){return extractClaim(token ,Claims:: getSubject);  }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}
