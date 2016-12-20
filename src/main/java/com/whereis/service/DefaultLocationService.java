package com.whereis.service;

import com.whereis.dao.DefaultLocationDao;
import com.whereis.model.User;
import com.whereis.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userLocationService")
@Transactional
public class DefaultLocationService implements LocationService {
    @Autowired
    DefaultLocationDao locationDao;

    @Override
    public Location get(int id) {
        return null;
    }

    @Override
    public void save(Location location) {
        locationDao.save(location);
    }

    @Override
    public void update(Location location) {

    }

    @Override
    public void delete(Location location) {

    }

    @Override
    public Location getLastLocationForUser(User user) {
        return locationDao.getLastLocationForUser(user);
    }
}
