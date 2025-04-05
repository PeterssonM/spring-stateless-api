package com.mape.portfolio_app.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    private final String fieldName;
    
    public JwtAuthenticationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public JwtAuthenticationException(String message, String fieldName, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
    
}
