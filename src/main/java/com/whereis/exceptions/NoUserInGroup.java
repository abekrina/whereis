package com.whereis.exceptions;

public class NoUserInGroup extends Exception {
    public NoUserInGroup(){
        super();
    }
    public NoUserInGroup(Exception e) {
        super(e);
    }
}
