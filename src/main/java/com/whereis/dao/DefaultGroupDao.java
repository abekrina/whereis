package com.whereis.dao;

import com.whereis.model.Group;
import org.hibernate.Session;

public class DefaultGroupDao extends AbstractDao<Group> implements GroupDao {
    @Override
    public void save(Group group) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(group);
    }

    @Override
    public void update(Group group) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(group);
    }
}
