package com.whereis.exceptions.GoogleAuthentication;

import com.whereis.exceptions.GoogleAuthentication.GoogleApiException;

public class GoogleApiNotAccessibleException extends GoogleApiException {
    public GoogleApiNotAccessibleException() {
        super();
    }

    public GoogleApiNotAccessibleException(Exception e) {
        super(e);
    }
}
