package com.whereis.service;

import com.whereis.dao.DefaultLocationDao;
import com.whereis.model.User;
import com.whereis.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userLocationService")
@Transactional
public class DefaultLocationService implements UserToGroupRelationService {
    @Autowired
    DefaultLocationDao userLocationDao;

    public Location getCurrentUserLocation(User user) {
        return userLocationDao.getLastLocationForUser(user);
    }

    public void saveUserLocation(Location location) {
        userLocationDao.save(location);
    }
}
