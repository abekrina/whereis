package com.whereis.service;

import com.whereis.dao.LocationDao;
import com.whereis.dao.UserDao;
import com.whereis.dao.UsersInGroupsDao;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userLocationService")
@Transactional
public class DefaultLocationService implements LocationService {
    @Autowired
    LocationDao locationDao;

    @Autowired
    UserDao usersDao;

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
    public boolean delete(Location location) {
        return locationDao.delete(location.getClass(), location.getId());
    }

    @Override
    public Location getLastLocationForUser(User user) {
        return locationDao.getLastLocationForUser(user);
    }

    @Override
    public List<Location> getLocationsOfGroupMembers(Group group, User currentUser) {
        if (usersDao.assertUserInGroup(group, currentUser)) {
            return locationDao.getLastLocationsForGroupMembers(group);
        }
        return new ArrayList<>();
    }
}
