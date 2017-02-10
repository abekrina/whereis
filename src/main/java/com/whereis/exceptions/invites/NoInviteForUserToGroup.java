package com.whereis.exceptions.invites;

public class NoInviteForUserToGroup extends Exception {
    public NoInviteForUserToGroup(String message){
        super(message);
    }
    public NoInviteForUserToGroup(Exception e) {
        super(e);
    }
}
