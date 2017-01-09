package com.whereis.dao;

import com.whereis.model.UsersInGroup;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("usersInGroupsDao")
public class DefaultUsersInGroupsDao extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {
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
