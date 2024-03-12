package com.romsystem.backend.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    private final SecretKey secretKey;
    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    public JwtTokenUtils(@Value("${jwt.secret}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifeTime.toMillis());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }
    private Claims getAllClaimsFromToken(String jws) {
        return
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jws)
                        .getPayload();
    }

    public String getEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Collection<? extends GrantedAuthority> getRoles(String jwt) {
        Claims claims = getAllClaimsFromToken(jwt);

        List<String> roles = claims.get("roles", List.class);

        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
