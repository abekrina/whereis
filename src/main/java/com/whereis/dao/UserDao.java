package com.whereis.dao;

import com.whereis.model.User;


public interface UserDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    User get(int id);
    void delete(User user);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(User user);
    void update(User user);
    User getByEmail(String email);
    void deleteByEmail(String email);
}
