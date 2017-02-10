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
import java.util.Set;

@Service("userLocationService")
@Transactional
public class DefaultLocationService implements LocationService {
    @Autowired
    LocationDao locationDao;

    @Autowired
    UserService userService;

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
        List<Location> locations = user.getLocations();
        if (locations.isEmpty()) {
            return null;
        } else {
            return locations.get(locations.size() - 1);
        }
    }

    @Override
    public List<Location> getLastLocationsForGroupMembers(Group group, User currentUser) {
        if (userService.checkUserInGroup(group, currentUser)) {
            Set<User> usersInGroup = group.getUsersInGroup();
            List<Location> locations = new ArrayList<>();
            for (User user : usersInGroup) {
                locations.add(getLastLocationForUser(user));
            }
            return locations;
        }
        return new ArrayList<>();
    }
}
