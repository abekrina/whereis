package com.whereis.exceptions.invites;

public abstract class InviteException extends Exception {
    public InviteException(String message){
        super(message);
    }
    public InviteException(Exception e) {
        super(e);
    }
}
