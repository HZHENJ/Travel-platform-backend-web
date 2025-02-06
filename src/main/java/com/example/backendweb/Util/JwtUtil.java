package com.example.backendweb.Util;

import com.example.backendweb.Entity.User.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import java.util.Date;


/**
 * @ClassName JwtUtil
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";
    private static final long EXPIRATION_TIME = 86400000; // 1 天

    public String generateToken(Integer userId, Authentication.Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Integer extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
