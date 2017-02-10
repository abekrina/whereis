package com.whereis.exceptions.groups;

public class NoUserInGroupException extends Exception {
    public NoUserInGroupException(String message){
        super(message);
    }
    public NoUserInGroupException(Exception e) {
        super(e);
    }
}
