package com.example.swapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomClaims {

    private String username;
    private Long userId;
    private UUID uuidToken;

}
