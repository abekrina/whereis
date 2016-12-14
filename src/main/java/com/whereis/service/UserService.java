package com.whereis.service;

import com.whereis.model.User;

public interface UserService {
    User findById(int id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUserByEmail(String email);
    User findUserByEmail(String email);
}
