package com.whereis.exceptions.groups;

public class UserAlreadyInGroup extends Exception {
    public UserAlreadyInGroup(String message){
        super(message);
    }
    public UserAlreadyInGroup(Exception e) {
        super(e);
    }
}
