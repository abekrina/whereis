package com.whereis.dao;

import com.whereis.model.Location;
import com.whereis.model.User;
//import com.whereis.model.UserLocation_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

@Repository("userLocationDao")
public class DefaultLocationDao extends AbstractDao<Integer, Location> implements LocationDao {
    public Location getCurrentLocationOfUser(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Location> query = createEntityCriteria();
        Root<Location> root = query.from(Location.class);
        query.select(root);

        //query.orderBy(builder.desc(root.get(UserLocation_.timestamp)));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void saveUserLocation(Location location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(location);
    }


}
