package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.NoUserInGroup;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Transactional
@Repository("usersInGroupsDao")
public class DefaultUsersInGroupsDao extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

    @Override
    public void save(UsersInGroup user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }

    @Override
    public void update(UsersInGroup user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
    }

    @Override
    public void leave(Group group, User user) throws NoUserInGroup {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UsersInGroup> criteriaQuery = createEntityCriteria();
        Root<UsersInGroup> usersInGroupRoot = criteriaQuery.from(UsersInGroup.class);
        criteriaQuery.select(usersInGroupRoot);
        criteriaQuery.where(builder.and(builder.equal(usersInGroupRoot.get("user_id"), user.getId())),
                builder.equal(usersInGroupRoot.get("group_id"), group.getId()));
        UsersInGroup relationToDelete = null;
        try {
            relationToDelete = getSession().createQuery(criteriaQuery).getSingleResult();
            delete(relationToDelete);
        } catch (NoResultException e) {
            logger.error("Group identity:" + group.getIdentity() + " name:" + group.getName());
            throw new NoUserInGroup(e);
        }
    }
}
