package com.whereis.exceptions;

public class GoogleApiNotAccessibleException extends GoogleApiException {
    public GoogleApiNotAccessibleException() {
        super();
    }

    public GoogleApiNotAccessibleException(Exception e) {
        super(e);
    }
}
