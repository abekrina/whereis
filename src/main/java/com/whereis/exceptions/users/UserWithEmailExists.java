package com.whereis.exceptions.users;

public class UserWithEmailExists extends Exception {
    public UserWithEmailExists(String message){
        super(message);
    }
    public UserWithEmailExists(Exception e) {
        super(e);
    }
}
