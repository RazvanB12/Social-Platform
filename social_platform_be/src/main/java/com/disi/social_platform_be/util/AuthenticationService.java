package com.disi.social_platform_be.util;

import com.disi.social_platform_be.exception.InvalidTokenException;
import com.disi.social_platform_be.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

@Component
public class AuthenticationService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.token.expiration-time}")
    private String tokenExpirationTime;
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$") ;

    public String generateToken(Map<String, String> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpirationTime)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            checkTokenProperties(token);
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
            return false;
        }
        return hasValidUid(token);
    }

    public Role extractRole(String token) {
        Claims claims = extractAllClaims(token);
        var role = (String) claims.get("user_role");

        if (role == null || role.isEmpty())
            throw new InvalidTokenException("Provided token is not valid");

        return role.equals("ADMIN")
                ? Role.ADMIN
                : Role.CLIENT;
    }

    public UUID extractId(String token) {
        var id = extractUserId(token);
        return UUID.fromString(id);
    }

    private void checkTokenProperties(String token) {
        extractClaim(token, Claims::getExpiration);
    }

    private boolean hasValidUid(String token) {
        return UUID_PATTERN.matcher(extractUserId(token)).matches();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("user_id");
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
