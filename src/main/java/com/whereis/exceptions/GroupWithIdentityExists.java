package com.whereis.exceptions;

public class GroupWithIdentityExists extends Exception {
    public GroupWithIdentityExists(String message){
        super(message);
    }
    public GroupWithIdentityExists(Exception e) {
        super(e);
    }
}
