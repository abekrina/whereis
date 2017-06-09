package com.whereis.authentication;
import org.springframework.security.core.AuthenticationException;

class GoogleAuthenticationException extends AuthenticationException {
    GoogleAuthenticationException(String message) {
        super(message);
    }
}
