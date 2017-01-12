package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
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
    @Override
    public void save(Group group) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(group);
    }

    @Override
    public void update(Group group) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(group);
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
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
