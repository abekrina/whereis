package com.whereis.service;

import com.whereis.model.Location;
import com.whereis.model.User;

public interface LocationService {
    Location get(int id);
    void save(Location location);
    void update(Location location);
    void delete(Location location);
    Location getLastLocationForUser(User user);
}
