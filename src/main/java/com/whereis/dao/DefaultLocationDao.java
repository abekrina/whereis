package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {

    @Override
    public void save(Location location) {
        getSession().persist(location);
    }

    @Override
    public Location getLastLocationForUser(User user) {
        try {
            List<Location> locations = user.getLocations();
            return locations.get(locations.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<Location> getLastLocationsForGroupMembers(Group group) {
        Set<User> usersInGroup = group.getUsersInGroup();
        List<Location> locations = new ArrayList<>();
        for (User user : usersInGroup) {
            locations.add(getLastLocationForUser(user));
        }
        return locations;
    }
}
