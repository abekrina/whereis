package com.whereis.exceptions.groups;

public class GroupWithIdentityExistsException extends GroupException {
    public GroupWithIdentityExistsException(String message){
        super(message);
    }
    public GroupWithIdentityExistsException(Exception e) {
        super(e);
    }
}
