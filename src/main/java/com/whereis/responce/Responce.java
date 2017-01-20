package com.whereis.responce;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responce extends ResponseEntity {
    public Responce(HttpStatus status) {
        super(status);
    }
}
