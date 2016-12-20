package com.whereis.dao;

import com.whereis.model.Location;
import com.whereis.model.User;

public interface LocationDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    Location get(int id);
    void delete(Location location);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Location location);
    Location getLastLocationForUser(User user);
}
