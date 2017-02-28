package com.whereis.exceptions.invites;

// TODO: discuss
// можно ли так делать? абстрактный эксепшн нужен, чтобы в тестах ловить один абстрактный,
// а не большую кучу конкретных эксепшенов
public abstract class InviteException extends Exception {
    public InviteException(String message){
        super(message);
    }
    public InviteException(Exception e) {
        super(e);
    }
}
