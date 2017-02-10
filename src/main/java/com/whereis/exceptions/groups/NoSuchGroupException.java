package com.whereis.exceptions.groups;

public class NoSuchGroupException extends Exception {
    public NoSuchGroupException(String message){
        super(message);
    }
    public NoSuchGroupException(Exception e) {
        super(e);
    }
}
