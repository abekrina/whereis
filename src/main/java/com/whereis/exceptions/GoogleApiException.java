package com.whereis.exceptions;

public class GoogleApiException extends Exception{
    public GoogleApiException() {
        super();
    }

    public GoogleApiException(Exception e) {
        super(e);
    }
}
