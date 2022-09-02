package com.example.swapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.swapi.dto.AuthenticationGrantType;
import com.example.swapi.dto.CustomClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    protected static final String USER_ID_CLAIM = "userId";
    protected static final String UUID_TOKEN_CLAIM = "uuid";
    protected static final String TOKEN_SCOPE = "scope";
    @Value("${jwt.com.example.jwtSecret}")
    protected String secret;

    public JwtTokenUtil() {
    }

    public String generateToken(String username) {
        Map<String, Object> newClames = new HashMap<>();
        newClames.put("sub", username);
        newClames.put("userId", 1L);
        newClames.put("uuid", UUID.randomUUID());
        newClames.put("scope", AuthenticationGrantType.CREDENTIALS.getValue());
        return Jwts.builder()
                .setSubject(username)
                .setClaims(newClames)
                .setIssuer("local")
                .setIssuedAt(new Date())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusYears(1L)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = this.getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token)
                && AuthenticationGrantType.CREDENTIALS.getValue().equals(this.getTokenScope(token));
    }

    public String getUsernameFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public CustomClaims getCustomClaims(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        return new CustomClaims(jwt.getSubject(),
                claims.get("userId").asLong(),
                claims.get("uuid").as(UUID.class));
    }

    public UUID getUuidFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("uuid");
            return claim != null ? claim.as(UUID.class) : null;
        } catch (JWTDecodeException var5) {
            log.error("Can't get claim uuid message: {}", var5.getMessage(), var5);
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("userId");
            log.debug("User id from token's claim is: {}", claim.asLong());
            return claim.asLong();
        } catch (JWTDecodeException var5) {
            log.error("Can't get claim userId message: {}", var5.getMessage(), var5);
            return null;
        }
    }

    protected String getTokenScope(String token) {
        return (String) this.getAllClaimsFromToken(token).get("scope");
    }

    public Date getExpirationDateFromToken(String token) {
        return (Date) this.getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    protected Claims getAllClaimsFromToken(String token) {
        return (Claims) Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    protected boolean isTokenExpired(String token) {
        Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}

