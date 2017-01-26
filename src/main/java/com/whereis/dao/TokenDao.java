package com.whereis.dao;

import com.whereis.model.Token;

public interface TokenDao {
    /**
     *  Methods are implemented in AbstractDao
     *  @see AbstractDao
     */
    Token get(int id);
    boolean delete(Class <? extends Token> type, int id);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Token token);
    void update(Token token);
}
