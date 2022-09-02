package com.example.swapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public JwtAuthenticationEntryPoint() {
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException {
        Map<String, Object> map = new HashMap();
        map.put("error", "401");
        map.put("error_description", authException.getMessage());
        map.put("code", 401);
        map.put("message", authException.getMessage());
        map.put("status", 401);
        response.setContentType("application/json");
        response.setStatus(401);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), map);
        } catch (Exception var6) {
            throw new ServletException();
        }
    }
}
