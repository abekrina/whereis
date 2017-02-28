package com.whereis.exceptions.GoogleAuthentication;

public class GoogleApiException extends GoogleAuthenticationException {
    public GoogleApiException(String message) {
        super(message);
    }
    public GoogleApiException(Exception e) {
        super(e);
    }
}
