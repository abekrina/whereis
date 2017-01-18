package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.exceptions.GroupWithIdentityExists;
import com.whereis.exceptions.NoSuchGroup;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
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
@Repository("groupDao")
public class DefaultGroupDao extends AbstractDao<Group> implements GroupDao {
    private static final Logger logger = LogManager.getLogger(DefaultGroupDao.class);

    @Override
    public void save(Group group) throws GroupWithIdentityExists {
        if (getByIdentity(group.getIdentity()) == null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.save(group);
        } else {
            throw new GroupWithIdentityExists(group.toString());
        }

    }

    @Override
    public void update(Group group) throws NoSuchGroup {
        if (get(group.getId()) != null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.update(group);
        } else {
            throw new NoSuchGroup(group.toString());
        }
    }

    @Override
    public Group getByIdentity(String identity) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Group> criteriaQuery = createEntityCriteria();
        Root<Group> groupRoot = criteriaQuery.from(Group.class);
        criteriaQuery.select(groupRoot);
        criteriaQuery.where(builder.equal(groupRoot.get("identity"), identity));
        try {
            return getSession().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
