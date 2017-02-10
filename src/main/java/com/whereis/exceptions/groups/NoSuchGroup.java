package com.whereis.exceptions.groups;

public class NoSuchGroup extends Exception {
    public NoSuchGroup(String message){
        super(message);
    }
    public NoSuchGroup(Exception e) {
        super(e);
    }
}
