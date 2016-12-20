package com.whereis.service;

import com.whereis.model.User;

public interface UserService {
    User get(int id);
    void save(User user);
    void update(User user);
    void delete(User user);

    //TODO: delete next two methods if not needed
    User getByEmail(String email);
    void deleteByEmail(String email);
}
