package com.example.swapi.dto;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
public class TokenUser extends User {

    private static final long serialVersionUID = -175661956594888754L;
    private Long userId;
    private String token;
    private UUID uuidToken;

    public TokenUser(String login, String pwd, Long userId, boolean isBlocked, Collection<? extends GrantedAuthority> authorities, String token, UUID uuidToken) {
        super(login, pwd, true, true, true, !isBlocked, authorities);
        this.userId = userId;
        this.token = token;
        this.uuidToken = uuidToken;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getUuidToken() {
        return uuidToken;
    }

    public void setUuidToken(UUID uuidToken) {
        this.uuidToken = uuidToken;
    }
}