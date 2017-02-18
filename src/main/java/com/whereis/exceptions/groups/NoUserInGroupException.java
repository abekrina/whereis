package com.whereis.exceptions.groups;

public class NoUserInGroupException extends GroupException {
    public NoUserInGroupException(String message){
        super(message);
    }
    public NoUserInGroupException(Exception e) {
        super(e);
    }
}
