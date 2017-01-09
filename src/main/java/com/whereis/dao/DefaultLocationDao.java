package com.whereis.dao;

import com.whereis.model.Location;
import com.whereis.model.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {
    @Override
    public void save(Location location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(location);
    }

    @Override
    public Location getLastLocationForUser(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Location> query = createEntityCriteria();
        Root<Location> root = query.from(Location.class);
        query.select(root);
        query.where(builder.equal(root.get("userId"), user.getId()));
        query.orderBy(builder.desc(root.get("timestamp")));

        try {
            return entityManager.createQuery(query).getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
