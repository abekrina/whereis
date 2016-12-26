package com.whereis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpSession;

abstract class AbstractController {

    @Autowired
    protected HttpSession httpSession;

    @Value("${google.client.id}")
    String CLIENT_ID;

    @Value("${google.client.secret}")
    String CLIENT_SECRET;
}
