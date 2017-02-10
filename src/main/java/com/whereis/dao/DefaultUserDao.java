package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.users.NoSuchUser;
import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.users.UserWithEmailExists;
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
    public void save(User user) throws UserWithEmailExists {
        if (getByEmail(user.getEmail()) == null) {
            getSession().persist(user);
        } else {
            throw new UserWithEmailExists(user.getEmail());
        }
    }

    @Override
    public void update(User user) throws NoSuchUser {
        if (get(user.getId()) != null) {
            getSession().update(user);
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
        User user = getByEmail(email);
        delete(user.getClass(), user.getId());
    }

    @Override
    public void leaveGroup(Group group, User user) throws NoUserInGroup {
        user.leave(group);
    }

    @Override
    public void joinGroup(Group group, User user) throws UserAlreadyInGroup {
        user.joinGroup(group);
    }

    //TODO: move to service
    // TODO: rename to checkUserInGroup
    @Override
    public boolean assertUserInGroup(Group group, User user) {
        return user.getGroups().contains(group);
    }

    @Override
    public Set<Group> getGroupsForUser(User user) {
        return user.getGroups();
    }

    //TODO: move to service
    @Override
    public void saveUserLocation(Location location, User user) {
        user.saveUserLocation(location);
    }
}
