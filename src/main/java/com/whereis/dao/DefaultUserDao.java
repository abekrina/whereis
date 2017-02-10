package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Set;

@Transactional
@Repository("userDao")
public class DefaultUserDao extends AbstractDao<User> implements UserDao {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

    @Override
    public void save(User user) throws UserWithEmailExistsException {
        if (getByEmail(user.getEmail()) == null) {
            getSession().persist(user);
        } else {
            throw new UserWithEmailExistsException(user.getEmail());
        }
    }

    @Override
    public void update(User user) throws NoSuchUserException {
        if (get(user.getId()) != null) {
            getSession().update(user);
        } else {
            throw new NoSuchUserException(user.toString());
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
        User user = getByEmail(email);
        delete(user.getClass(), user.getId());
    }
}
