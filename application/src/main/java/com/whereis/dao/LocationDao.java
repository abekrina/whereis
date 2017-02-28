package com.whereis.dao;

import com.whereis.model.Location;

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

    void refresh(Location location);
}
