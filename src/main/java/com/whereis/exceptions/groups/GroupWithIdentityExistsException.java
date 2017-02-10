package com.whereis.exceptions.groups;

public class GroupWithIdentityExistsException extends Exception {
    public GroupWithIdentityExistsException(String message){
        super(message);
    }
    public GroupWithIdentityExistsException(Exception e) {
        super(e);
    }
}
