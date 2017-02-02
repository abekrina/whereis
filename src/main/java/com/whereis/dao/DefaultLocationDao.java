package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.UsersInGroup;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {
    @Override
    public void save(Location location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(location);
    }

    @Override
    public Location getLastLocationForUser(int userId) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Location> query = createEntityCriteria();
        Root<Location> root = query.from(Location.class);
        query.select(root);
        query.where(builder.equal(root.get("user_id"), userId));
        query.orderBy(builder.desc(root.get("timestamp")));
        try {
            return getSession().createQuery(query).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
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
            locations.add(getLastLocationForUser(user.getUserId()));
        }

        return locations;
    }
}
