package com.bsmm.login.config;

import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.service.dto.impl.UserDetailsImpl;
import com.bsmm.login.util.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expiration-at}")
    private int expirationAccessToken;

    @Value("${jwt.token.expiration-rt}")
    private int expirationRefreshToken;

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(depureToken(token)).getBody().getSubject();
    }

    public String getClaimId(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(depureToken(token)).getBody().getId();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(depureToken(token));
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private String generateToken(UserDetailsImpl details, long expiration) {
        List<String> roles = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder().setSubject(details.getUsername())
                .claim(Constants.CLAIM_EMAIL, details.getEmail())
                .claim(Constants.CLAIM_NAME, details.getUsername())
                .claim(Constants.CLAIM_ROLES, roles)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date()).setExpiration(new Date(expiration))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private String depureToken(String token) {
        if (token == null) {
            return null;
        }
        return token.replace(Constants.TOKEN_TYPE_WITH_SPACE, "");
    }
    public LoginResponse getResponse(UserDetailsImpl details) {
        long expirationAT = System.currentTimeMillis() + expirationAccessToken;
        return LoginResponse.builder().tokenType(Constants.TOKEN_TYPE)
                .accessToken(generateToken(details, expirationAT))
                .refreshToken(generateToken(details, System.currentTimeMillis() + expirationRefreshToken))
                .expires(expirationAT)
                .build();
    }
}
