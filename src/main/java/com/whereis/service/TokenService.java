package com.whereis.service;

import com.whereis.model.Token;

public interface TokenService {
    Token get(int id);
    void save(Token token);
    void update(Token token);
    void delete(Token token);
}
