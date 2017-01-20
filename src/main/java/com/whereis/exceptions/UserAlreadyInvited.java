package com.whereis.exceptions;

public class UserAlreadyInvited extends Exception {
    public UserAlreadyInvited(String message){
        super(message);
    }
    public UserAlreadyInvited(Exception e) {
        super(e);
    }
}
