package com.whereis.dao;

import com.whereis.model.User;
import com.whereis.model.UserLocation;
//import com.whereis.model.UserLocation_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

@Repository("userLocationDao")
public class UserLocationDaoImpl extends AbstractDao<Integer, UserLocation> implements UserLocationDao{
    public UserLocation getCurrentLocationOfUser(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UserLocation> query = createEntityCriteria();
        Root<UserLocation> root = query.from(UserLocation.class);
        query.select(root);

        //query.orderBy(builder.desc(root.get(UserLocation_.timestamp)));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void saveUserLocation(UserLocation location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(location);
    }


}
