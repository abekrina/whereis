package com.whereis.exceptions.users;

public class NoSuchUser extends Exception {
    public NoSuchUser(String message){
        super(message);
    }
    public NoSuchUser(Exception e) {
        super(e);
    }
}
