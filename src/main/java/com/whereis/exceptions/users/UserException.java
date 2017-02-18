package com.whereis.exceptions.users;

public abstract class UserException extends Exception {
    public UserException(String message){
        super(message);
    }
    public UserException(Exception e) {
        super(e);
    }
}
