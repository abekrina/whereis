package com.whereis.dao;

import com.whereis.model.UserToGroupRelation;
import org.hibernate.Session;

public class DefaultUserToGroupRelation extends AbstractDao<UserToGroupRelation> implements UserToGroupRelationDao{
    @Override
    public void save(UserToGroupRelation user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }

    @Override
    public void update(UserToGroupRelation user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
    }
}
