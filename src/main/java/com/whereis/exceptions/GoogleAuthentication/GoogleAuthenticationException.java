package com.whereis.exceptions.GoogleAuthentication;

public abstract class GoogleAuthenticationException extends Exception {
    public GoogleAuthenticationException(String message){
        super(message);
    }
    public GoogleAuthenticationException(Exception e) {
        super(e);
    }
}
