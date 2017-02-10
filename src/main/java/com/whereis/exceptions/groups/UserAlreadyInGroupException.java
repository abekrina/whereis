package com.whereis.exceptions.groups;

public class UserAlreadyInGroupException extends Exception {
    public UserAlreadyInGroupException(String message){
        super(message);
    }
    public UserAlreadyInGroupException(Exception e) {
        super(e);
    }
}
