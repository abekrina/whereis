package com.whereis.exceptions.GoogleAuthentication;

public class GoogleApiNotAccessibleException extends GoogleApiException {
    public GoogleApiNotAccessibleException(String message) {
        super(message);
    }
    public GoogleApiNotAccessibleException(Exception e) {
        super(e);
    }
}
