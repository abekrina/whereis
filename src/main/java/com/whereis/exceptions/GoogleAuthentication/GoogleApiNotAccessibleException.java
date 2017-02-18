package com.whereis.exceptions.GoogleAuthentication;

import com.whereis.exceptions.GoogleAuthentication.GoogleApiException;

public class GoogleApiNotAccessibleException extends GoogleApiException {
    public GoogleApiNotAccessibleException(String message) {
        super(message);
    }
    public GoogleApiNotAccessibleException(Exception e) {
        super(e);
    }
}
