package com.whereis.dao;

import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.User;


public interface UserDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    User get(int id);
    boolean delete(Class <? extends User> type, int id);
    /**
     *  Methods specific for every implementation of this interface
     */
    void save(User user) throws UserWithEmailExistsException;
    void update(User user) throws NoSuchUserException;

    void merge(User user);

    void refresh(User user);

    User getByEmail(String email);
    void deleteByEmail(String email);
}
