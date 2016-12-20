package com.whereis.dao;

import com.whereis.model.Token;

public interface TokenDao {
    /**
     *  Methods are implemented in AbstractDao
     *  @see AbstractDao
     */
    Token get(int id);
    void delete(Token token);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Token token);
    void update(Token token);
}
