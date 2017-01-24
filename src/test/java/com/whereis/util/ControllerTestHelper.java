package com.whereis.util;

import com.whereis.authentication.GoogleAuthentication;
import com.whereis.model.User;

public class ControllerTestHelper {

    public static GoogleAuthentication getTestAuthentication(int id, String email, String firstName, String lastName) {
        User testUser = new User();
        testUser.setId(id);
        testUser.setEmail(email);
        testUser.setFirstName(firstName);
        testUser.setLastName(lastName);
        GoogleAuthentication authentication = new GoogleAuthentication("123", "123");
        authentication.setPrincipal(testUser);
        authentication.setAuthenticated(true);
        return authentication;
    }
}
