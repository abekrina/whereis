package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Location;

import java.util.List;

public interface LocationDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    Location get(int id);
    boolean delete(Class <? extends Location> type, int id);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Location location);
    Location getLastLocationForUser(int userId);

    List<Location> getLastLocationsForGroupMembers(Group group);
}
