package com.whereis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

@RestController
public class IndexController extends AbstractController {

    @RequestMapping(value = "/config.json", method = RequestMethod.GET)
    public HashMap<String, String> getConfig() {
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        HashMap<String, String> config = new HashMap<>();

        httpSession.setAttribute("state", state);
        config.put("clientId", CLIENT_ID);
        config.put("state", state);

        return config;
    }
}
