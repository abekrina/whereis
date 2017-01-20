package com.whereis.exceptions;

public class UserAlreadyInGroup extends Exception {
    public UserAlreadyInGroup(String message){
        super(message);
    }
    public UserAlreadyInGroup(Exception e) {
        super(e);
    }
}
