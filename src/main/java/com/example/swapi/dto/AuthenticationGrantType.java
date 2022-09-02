package com.example.swapi.dto;

public enum AuthenticationGrantType {
    CREDENTIALS("credentials"),
    REFRESH_TOKEN("refresh_token"); // todo add usage

    private String value;

    private AuthenticationGrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
