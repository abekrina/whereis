package com.whereis.exceptions.users;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super();
    }
    public UserNotFoundException(Exception e) {
        super(e);
    }
}
