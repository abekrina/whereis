package com.whereis.dao;

import com.whereis.model.User;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {
    @Override
    public User findById(int id) {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User) currentSession.createQuery("FROM User WHERE user_id = :userIdParam")
                .setParameter("userIdParam", id)
                .getSingleResult();
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User) currentSession.createQuery("FROM User WHERE user_email = :userEmailParam")
                .setParameter("userEmailParam", email)
                .getSingleResult();
        return user;
    }

    @Override
    public void deleteUserByEmail(String email) {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User) currentSession.createQuery("FROM User WHERE user_email = :userEmailParam")
                .setParameter("userEmailParam", email)
                .getSingleResult();
        delete(user);
    }

    @Override
    public void saveUser(User user) {
        if (findUserByEmail(user.getUser_email()) == null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.saveOrUpdate(user);
        }
    }
}
