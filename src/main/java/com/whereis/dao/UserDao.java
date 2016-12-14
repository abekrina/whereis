package com.whereis.dao;

import com.whereis.model.User;


public interface UserDao {
    User findById(int id);
    User findUserByEmail(String email);
    void saveUser(User user);
    void deleteUserByEmail(String email);
}
