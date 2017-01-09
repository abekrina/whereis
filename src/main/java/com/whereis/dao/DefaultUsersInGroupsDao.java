package com.whereis.dao;

import com.whereis.model.UsersInGroup;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class DefaultUsersInGroups extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {
    @Override
    public void save(UsersInGroup user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }

    @Override
    public void update(UsersInGroup user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
    }
}
