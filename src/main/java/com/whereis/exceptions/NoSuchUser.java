package com.whereis.exceptions;

public class NoSuchUser extends Exception {
    public NoSuchUser(String message){
        super(message);
    }
    public NoSuchUser(Exception e) {
        super(e);
    }
}
