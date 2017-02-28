package com.whereis.dao;

import com.whereis.model.Location;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {

    @Override
    public void save(Location location) {
        getSession().persist(location);
    }

    @Override
    public void refresh(Location location) {
        getSession().refresh(location);
    }
}
