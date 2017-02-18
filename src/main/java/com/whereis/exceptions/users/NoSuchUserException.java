package com.whereis.exceptions.users;

public class NoSuchUserException extends UserException {
    public NoSuchUserException(String message){
        super(message);
    }
    public NoSuchUserException(Exception e) {
        super(e);
    }
}
