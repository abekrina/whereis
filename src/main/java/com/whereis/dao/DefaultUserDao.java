package com.whereis.dao;

import com.whereis.model.User;

//import com.whereis.model.User_;
import org.hibernate.Session;

import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("userDao")
public class DefaultUserDao extends AbstractDao<Integer, User> implements UserDao {
    @Override
    public User findUserByEmail(String email) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<User> criteriaQuery = createEntityCriteria();
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
//        criteriaQuery.where(builder.equal(userRoot.get(User_.email), email));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteUserByEmail(String email) {
        delete(findUserByEmail(email));
    }

    @Override
    public void saveUser(User user) {
        if (findUserByEmail(user.getEmail()) == null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.saveOrUpdate(user);
        }
    }

    //update()


}
