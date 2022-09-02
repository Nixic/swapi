package com.example.swapi.dto;

import lombok.Data;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@Data
public class AuthParams {

    // todo change fields as per dto
    private final String requestTokenHeader;
    private final HttpServletRequest request;
    private String username;
    private Long userId;
    private UUID uuidToken;
    private String jwtToken;

}
