package com.whereis.service;

import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;

import java.util.List;

public interface LocationService {
    Location get(int id);
    void save(Location location);
    void update(Location location);
    void delete(Location location);
    Location getLastLocationForUser(User user);
    List<Location> getLocationsOfGroupMembers(Group group, User currentUser);
}
