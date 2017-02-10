package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.model.UsersInGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("usersInGroupsDao")
public class DefaultUsersInGroupsDao extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

/*    @Override
    public void save(UsersInGroup user) throws UserAlreadyInGroup {
        user.setJoinedAt(new Timestamp(System.currentTimeMillis()));
        if (findUserInGroup(user) != null) {
            throw new UserAlreadyInGroup("User is present in group: ");
        }
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
    }*/

/*    @Override
    public void leave(Group group, User user) throws NoUserInGroup {
        UsersInGroup requestedUserInGroup = new UsersInGroup();
        requestedUserInGroup.setUser(user);
        requestedUserInGroup.setGroup(group);

        UsersInGroup relationToDelete = findUserInGroup(requestedUserInGroup);
        if (relationToDelete == null) {
            logger.error("Group identity:" + group.getIdentity() + " name:" + group.getName());
            throw new NoUserInGroup(user.toString());
        } else {
            delete(relationToDelete.getClass(), relationToDelete.getId());
        }
    }*/

    // Returns null if not found
/*    @Override
    public UsersInGroup findUserInGroup(UsersInGroup usersInGroup) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UsersInGroup> criteriaQuery = createEntityCriteria();
        Root<UsersInGroup> usersInGroupRoot = criteriaQuery.from(UsersInGroup.class);
        criteriaQuery.select(usersInGroupRoot);
        criteriaQuery.where(builder.and(builder.equal(usersInGroupRoot.get("user"), usersInGroup.getUser())),
                builder.equal(usersInGroupRoot.get("group"), usersInGroup.getGroup()));
        UsersInGroup relation = null;
        try {
            relation = getSession().createQuery(criteriaQuery).getSingleResult();
            return relation;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UsersInGroup findUserInGroup(Group group, User user) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<UsersInGroup> criteriaQuery = createEntityCriteria();
        Root<UsersInGroup> usersInGroupRoot = criteriaQuery.from(UsersInGroup.class);
        criteriaQuery.select(usersInGroupRoot);
        criteriaQuery.where(builder.and(builder.equal(usersInGroupRoot.get("user_id"), user.getId())),
                builder.equal(usersInGroupRoot.get("group_id"), group.getId()));
        UsersInGroup relation = null;
        try {
            relation = getSession().createQuery(criteriaQuery).getSingleResult();
            return relation;
        } catch (NoResultException e) {
            return null;
        }
    }*/
}
