package com.whereis.controller;

import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.http.HttpSession;

abstract class AbstractController {

    @Autowired
    protected HttpSession httpSession;

    @Value("${google.client.id}")
    String CLIENT_ID;

    @Value("${google.client.secret}")
    String CLIENT_SECRET;

    protected User getCurrentUser() {
        SecurityContext context = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication auth = context.getAuthentication();
        return (User) auth.getPrincipal();
    }
}
