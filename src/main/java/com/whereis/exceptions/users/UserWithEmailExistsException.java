package com.whereis.exceptions.users;

public class UserWithEmailExistsException extends UserException {
    public UserWithEmailExistsException(String message){
        super(message);
    }
    public UserWithEmailExistsException(Exception e) {
        super(e);
    }
}
