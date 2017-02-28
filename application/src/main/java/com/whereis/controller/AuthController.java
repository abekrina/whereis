package com.whereis.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
public class AuthController extends AbstractController {

    @RequestMapping(value = "/config.json", method = RequestMethod.GET)
    public HashMap<String, String> getConfig() {
        HashMap<String, String> config = new HashMap<>();
        config.put("clientId", CLIENT_ID);
        return config;
    }

    @RequestMapping("/login")
    public void login() {
        int i = 0;
    }

    @RequestMapping(value="/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Logout successful";
    }
}
