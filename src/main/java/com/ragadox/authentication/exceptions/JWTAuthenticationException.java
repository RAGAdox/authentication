package com.ragadox.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationException extends AuthenticationException {

    public JWTAuthenticationException() {
        super("INVALID_TOKEN");
    }
    public JWTAuthenticationException(String msg) {
        super(msg);
    }
}
