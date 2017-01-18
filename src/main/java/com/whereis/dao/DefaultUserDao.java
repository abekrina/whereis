package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.NoSuchUser;
import com.whereis.exceptions.UserWithEmailExists;
import com.whereis.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

    @Override
    public Session getSession() {
        return super.getSession();
    }

    @Override
    public void save(User user) throws UserWithEmailExists {
        if (getByEmail(user.getEmail()) == null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.save(user);
        } else {
            throw new UserWithEmailExists(user.getEmail());
        }
    }

    @Override
    public void update(User user) throws NoSuchUser {
        if (get(user.getId()) != null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.update(user);
        } else {
            throw new NoSuchUser(user.toString());
        }
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
            return getSession().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteByEmail(String email) {
        delete(getByEmail(email));
    }
}
