package com.whereis.exceptions;

public class NoUserInGroup extends Exception {
    public NoUserInGroup(String message){
        super(message);
    }
    public NoUserInGroup(Exception e) {
        super(e);
    }
}
