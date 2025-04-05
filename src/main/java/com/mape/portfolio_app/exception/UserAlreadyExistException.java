package com.mape.portfolio_app.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown during the registration process (PersonService.java) whenever the userdetails email is not unique. 
 */
public class UserAlreadyExistException extends AuthenticationException {
    private final String fieldName;

    /**
     * Creates a new instance with the specified message.
     * @param message Contains the cause of the error.
     * @param fieldName Contains the field that was not unique (email, personal number or username). 
     */
    public UserAlreadyExistException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    /**
     * @return The fieldName variable. 
     */
    public String getFieldName() {
        return fieldName;
    }
}
