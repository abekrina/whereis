package com.whereis.service;

import com.whereis.dao.UserToGroupRelationDao;
import com.whereis.model.UserToGroupRelation;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultUserToGroupRelationService implements UserToGroupRelationService {
    @Autowired
    UserToGroupRelationDao dao;

    @Override
    public UserToGroupRelation get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(UserToGroupRelation user) {
        dao.save(user);
    }

    @Override
    public void update(UserToGroupRelation user) {
        dao.update(user);
    }

    @Override
    public void delete(UserToGroupRelation user) {
        dao.delete(user);
    }
}
