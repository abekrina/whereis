package com.whereis.dao;

import com.whereis.exceptions.NoSuchUser;
import com.whereis.exceptions.UserWithEmailExists;
import com.whereis.model.User;
import org.hibernate.Session;

import javax.persistence.EntityManager;


public interface UserDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    User get(int id);
    boolean delete(Class <? extends User> type, int id);
    Session getSession();
    /**
     *  Methods specific for every implementation of this interface
     */
    void save(User user) throws UserWithEmailExists;
    void update(User user) throws NoSuchUser;
    User getByEmail(String email);
    void deleteByEmail(String email);
}
