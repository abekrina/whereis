package com.whereis.service;

import com.whereis.dao.UsersInGroupsDao;
import com.whereis.model.UsersInGroup;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultUsersInGroupsService implements UserToGroupRelationService {
    @Autowired
    UsersInGroupsDao dao;

    @Override
    public UsersInGroup get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(UsersInGroup user) {
        dao.save(user);
    }

    @Override
    public void update(UsersInGroup user) {
        dao.update(user);
    }

    @Override
    public void delete(UsersInGroup user) {
        dao.delete(user);
    }
}
