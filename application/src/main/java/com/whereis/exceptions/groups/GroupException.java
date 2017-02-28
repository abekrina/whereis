package com.whereis.exceptions.groups;

public abstract class GroupException extends Exception {
    public GroupException(String message){
        super(message);
    }
    public GroupException(Exception e) {
        super(e);
    }
}
