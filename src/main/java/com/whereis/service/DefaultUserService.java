package com.whereis.service;

import com.whereis.dao.UserDao;
import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class DefaultUserService implements UserService {
    @Autowired
    public UserDao dao;

    @Override
    public User get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(User user) {
        dao.save(user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User getByEmail(String email) {
        return dao.getByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        dao.deleteByEmail(email);
    }
}
