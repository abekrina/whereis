package com.whereis.service;

import com.whereis.dao.UserDao;
import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    public UserDao dao;

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public void saveUser(User user) {
        dao.saveUser(user);
    }

    @Override
    public void updateUser(User user) {

    }
    @Override
    public User findUserByEmail(String email) {
        return dao.findUserByEmail(email);
    }

    @Override
    public void deleteUserByEmail(String email) {

    }
}
