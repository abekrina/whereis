package com.whereis.exceptions.users;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message){
        super(message);
    }
    public UserNotFoundException(Exception e) {
        super(e);
    }
}
