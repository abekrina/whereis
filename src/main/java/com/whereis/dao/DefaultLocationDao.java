package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {
    @Autowired
    UserDao userDao;

    @Override
    public void save(Location location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.persist(location);
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

        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UsersInGroup> usersInGroupCriteriaQuery = createEntityCriteria();
        Root<UsersInGroup> root = usersInGroupCriteriaQuery.from(UsersInGroup.class);
        usersInGroupCriteriaQuery.select(root);
        usersInGroupCriteriaQuery.where(builder.equal(root.get("group_id"), group.getId()));
        List<UsersInGroup> usersInGroup = getSession().createQuery(usersInGroupCriteriaQuery).getResultList();

        List<Location> locations = new ArrayList<>();
        for (UsersInGroup user : usersInGroup) {
            locations.add(getLastLocationForUser(userDao.get(user.getUserId())));
        }
        return locations;
    }
}
