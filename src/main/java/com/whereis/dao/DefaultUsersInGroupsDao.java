package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.NoUserInGroup;
import com.whereis.exceptions.UserAlreadyInGroup;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
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
    public void save(UsersInGroup user) throws UserAlreadyInGroup {
        if (findRelationInDB(user) != null) {
            throw new UserAlreadyInGroup("User is present in group: ");
        }
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }

    @Override
    public void update(UsersInGroup user) {
        if (get(user.getId()) != null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.update(user);
        }
    }

    @Override
    public void leave(Group group, User user) throws NoUserInGroup {
        UsersInGroup requestedUserInGroup = new UsersInGroup();
        requestedUserInGroup.setUserId(user.getId());
        requestedUserInGroup.setGroupId(group.getId());

        UsersInGroup relationToDelete = findRelationInDB(requestedUserInGroup);
        if (relationToDelete == null) {
            logger.error("Group identity:" + group.getIdentity() + " name:" + group.getName());
            throw new NoUserInGroup(user.toString());
        } else {
            delete(relationToDelete);
        }
    }

    // Returns null if not found
    @Override
    public UsersInGroup findRelationInDB (UsersInGroup usersInGroup) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UsersInGroup> criteriaQuery = createEntityCriteria();
        Root<UsersInGroup> usersInGroupRoot = criteriaQuery.from(UsersInGroup.class);
        criteriaQuery.select(usersInGroupRoot);
        criteriaQuery.where(builder.and(builder.equal(usersInGroupRoot.get("user_id"), usersInGroup.getUserId())),
                builder.equal(usersInGroupRoot.get("group_id"), usersInGroup.getGroupId()));
        UsersInGroup relation = null;
        try {
            relation = getSession().createQuery(criteriaQuery).getSingleResult();
            return relation;
        } catch (NoResultException e) {
            return null;
        }
    }
}
