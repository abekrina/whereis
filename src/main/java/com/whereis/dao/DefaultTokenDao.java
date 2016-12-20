package com.whereis.dao;

import com.whereis.model.Token;
import org.hibernate.Session;

public class DefaultTokenDao extends AbstractDao<Token> implements TokenDao {
    @Override
    public void save(Token token) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(token);
    }

    @Override
    public void update(Token token) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(token);
    }
}
