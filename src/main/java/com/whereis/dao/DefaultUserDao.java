package com.whereis.dao;

import com.whereis.model.User;

import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Transactional
@Repository("userDao")
public class DefaultUserDao extends AbstractDao<User> implements UserDao {
    @Override
    public Session getSession() {
        return super.getSession();
    }

    @Override
    public void save(User user) {
        if (getByEmail(user.getEmail()) == null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.save(user);
        }
    }

    @Override
    public void update(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
    }

    @Override
    public User getByEmail(String email) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<User> criteriaQuery = createEntityCriteria();
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
        criteriaQuery.where(builder.equal(userRoot.get("email"), email));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteByEmail(String email) {
        delete(getByEmail(email));
    }
}
