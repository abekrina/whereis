package com.whereis.exceptions.invites;

public class UserAlreadyInvited extends Exception {
    public UserAlreadyInvited(String message){
        super(message);
    }
    public UserAlreadyInvited(Exception e) {
        super(e);
    }
}
