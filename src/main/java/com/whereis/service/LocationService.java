package com.whereis.service;

public interface LocationService {
    LocationService get(int id);
    void save(LocationService userToGroup);
    void update(LocationService userToGroup);
    void delete(LocationService userToGroup);
}
