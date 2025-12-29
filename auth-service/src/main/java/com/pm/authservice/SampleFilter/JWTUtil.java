package com.pm.authservice.SampleFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private static final String SECRET_KEY = "my_name_is_ayush_solanki";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


    // generate JWT token
    public String generateToken(String username, long expiryMinutes){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000))// converted to milli seconds
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtractdUsername(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch(JwtException ex){
            return null;
        }
    }
}

