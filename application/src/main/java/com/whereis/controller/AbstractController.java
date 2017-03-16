package com.whereis.controller;

import com.whereis.model.User;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

abstract class AbstractController {

    String CLIENT_ID = System.getenv("WHEREIS_GOOGLE_CLIENT_ID");

    @Autowired
    protected HttpSession httpSession;

    protected User getCurrentUser() {
        SecurityContext context = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication auth = context.getAuthentication();
        return (User) auth.getPrincipal();
    }
}
