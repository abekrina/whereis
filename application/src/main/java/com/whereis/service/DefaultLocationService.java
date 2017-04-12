package com.whereis.service;

import com.whereis.dao.GroupDao;
import com.whereis.dao.LocationDao;
import com.whereis.dao.UserDao;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.Location;
import org.hibernate.Hibernate;
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

    @Autowired
    GroupDao groupDao;

    @Override
    public Location get(int id) {
        return locationDao.get(id);
    }

    @Override
    public void save(Location location) {
        locationDao.save(location);
    }

    @Override
    public void refresh(Location location) {
        locationDao.refresh(location);
    }

    @Override
    public boolean delete(Location location) {
        return locationDao.delete(location.getClass(), location.getId());
    }

    @Override
    public Location getLastLocationForUser(User user) {
        user = usersDao.get(user.getId());
        List<Location> locations = user.getLocations();
        if (locations.isEmpty()) {
            return null;
        } else {
            return locations.get(locations.size() - 1);
        }
    }

    @Override
    @Transactional
    public List<Location> getLastLocationsForGroupMembers(Group group, User currentUser) {
        group = groupDao.get(group.getId());
        Set<User> usersInGroup = group.getUsersInGroup();
        List<Location> locations = new ArrayList<>();
        for (User user : usersInGroup) {
            //if (!user.equals(currentUser)) {
                Location location = getLastLocationForUser(user);
                if (location != null) {
                    location.setUser(usersDao.get(location.getUser().getId()));
                    Hibernate.initialize(location.getUser());
                    locations.add(location);
                }
           // }
        }
        return locations;
    }
}
