package com.whereis.service;

import com.whereis.dao.GroupDao;
import com.whereis.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
@Service("groupService")
@Transactional
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
        //TODO: Make identity shorter and document its size
        String token = new BigInteger(130, 2, random).toString(32);
        group.setIdentity(token);

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

    @Override
    public Group getByIdentity(String identity) {
        return dao.getByIdentity(identity);
    }
}
