package com.whereis.exceptions.invites;

public class UserAlreadyInvitedException extends InviteException {
    public UserAlreadyInvitedException(String message){
        super(message);
    }
    public UserAlreadyInvitedException(Exception e) {
        super(e);
    }
}
