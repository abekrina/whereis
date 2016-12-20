package com.whereis.service;

import com.whereis.dao.AbstractDao;
import com.whereis.dao.TokenDao;
import com.whereis.model.Token;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultTokenService extends AbstractDao<Token> implements TokenService {
    @Autowired
    TokenDao dao;

    @Override
    public void save(Token token) {
        dao.save(token);
    }

    @Override
    public void update(Token token) {
        dao.update(token);
    }
}
