package com.whereis.exceptions.invites;

public class NoInviteForUserToGroupException extends Exception {
    public NoInviteForUserToGroupException(String message){
        super(message);
    }
    public NoInviteForUserToGroupException(Exception e) {
        super(e);
    }
}
