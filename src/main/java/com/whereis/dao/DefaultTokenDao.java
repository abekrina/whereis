package com.whereis.dao;

import com.whereis.model.Token;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("tokenDao")
public class DefaultTokenDao extends AbstractDao<Token> implements TokenDao {
    @Override
    public void save(Token token) {
        getSession().persist(token);
    }

    @Override
    public void update(Token token) {
        getSession().update(token);
    }
}
