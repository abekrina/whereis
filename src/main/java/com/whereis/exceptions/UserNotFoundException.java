package com.whereis.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super();
    }
    public UserNotFoundException(Exception e) {
        super(e);
    }
}
