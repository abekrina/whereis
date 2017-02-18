package com.whereis.exceptions.groups;

public class UserAlreadyInGroupException extends GroupException {
    public UserAlreadyInGroupException(String message){
        super(message);
    }
    public UserAlreadyInGroupException(Exception e) {
        super(e);
    }
}
