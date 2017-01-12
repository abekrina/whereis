package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.whereis.exceptions.GoogleApiException;
import com.whereis.exceptions.UserNotFoundException;
import com.whereis.model.User;

import java.io.IOException;

public interface UserService {
    User get(int id);
    void save(User user);
    void update(User user);
    void delete(User user);
    User getByEmail(String email);
    void deleteByEmail(String email);
    User createGoogleUser(Plus plus) throws IOException;
}
