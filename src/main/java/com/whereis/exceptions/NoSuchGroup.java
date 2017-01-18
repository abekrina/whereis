package com.whereis.exceptions;

public class NoSuchGroup extends Exception {
    public NoSuchGroup(String message){
        super(message);
    }
    public NoSuchGroup(Exception e) {
        super(e);
    }
}
