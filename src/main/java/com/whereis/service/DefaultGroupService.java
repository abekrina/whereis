package com.whereis.service;

import com.whereis.dao.GroupDao;
import com.whereis.model.Group;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;

public class DefaultGroupService implements GroupService {
    @Autowired
    GroupDao dao;

    @Override
    public Group get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(Group group) {
        SecureRandom random = new SecureRandom();
        String token = new BigInteger(130, 2, random).toString(32);
        group.setToken(token);

        dao.save(group);
    }

    @Override
    public void update(Group group) {
        dao.update(group);
    }

    @Override
    public void delete(Group group) {
        dao.delete(group);
    }
}
