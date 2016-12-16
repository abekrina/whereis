package com.whereis.dao;

import com.whereis.model.User;
import com.whereis.model.UserLocation;
import com.whereis.model.UserLocation_;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

public class UserLocationDaoImpl extends AbstractDao<Integer, UserLocation> implements UserLocationDao{
/*    public UserLocation getCurrentLocationOfUser(User user) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UserLocation> criteriaQuery = createEntityCriteria();
        Root<UserLocation> userLocationRoot = criteriaQuery.from(UserLocation.class);
        criteriaQuery.select(userLocationRoot);
        //Join<User, UserLocation> join = userLocationRoot.join(UserLocation_.userId);
        //criteriaQuery.where(builder.equal(join.get(UserLocation_.userId), user.getId()));

        criteriaQuery.(builder.greatest(userLocationRoot.get(UserLocation_.timestamp)));
        criteriaQuery.select(builder.greatest(userLocationRoot.get(UserLocation_.timestamp)));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }*/
}
