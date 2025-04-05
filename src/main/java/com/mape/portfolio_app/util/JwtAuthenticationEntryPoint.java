package com.mape.portfolio_app.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mape.portfolio_app.exception.JwtAuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errorMessage = "Please provide a valid token.";

        //System.out.println(authException.getClass().getSimpleName());
        //System.out.println(authException.getMessage()); 

        if (authException.getClass().equals(InsufficientAuthenticationException.class)) {
            errorMessage = "Insufficient privileges.";
        }
        if (authException.getClass().equals(JwtAuthenticationException.class)) {
            errorMessage = authException.getMessage();
        }
        
        System.out.println("\n" + errorMessage + "\n");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(
            new ObjectMapper().writeValueAsString(
                Map.of("error_code", "401", "message", "Unauthorized access. " + errorMessage)
            )
        );
        
    }
}

