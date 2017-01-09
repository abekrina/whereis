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
import java.sql.Timestamp;
import java.util.Calendar;

@Transactional
@Repository("inviteDao")
public class DefaultInviteDao extends  AbstractDao<Invite> implements InviteDao {

    @Override
    public void save(Invite invite) {
        if (invite.getTimestamp() == null) {
            invite.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(invite);
    }

    @Override
    public void update(Invite invite) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(invite);
    }

    @Override
    public Invite getSameInvite(Invite invite) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Invite> criteriaQuery = createEntityCriteria();
        Root<Invite> inviteRoot = criteriaQuery.from(Invite.class);
        criteriaQuery.select(inviteRoot);
        criteriaQuery.where(builder.and(builder.equal(inviteRoot.get("sent_to_email"), invite.getSentToEmail())),
                builder.equal(inviteRoot.get("group_id"), invite.getGroupId()));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Invite getPendingInviteFor(User user, Group group) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Invite> criteriaQuery = createEntityCriteria();
        Root<Invite> inviteRoot = criteriaQuery.from(Invite.class);
        criteriaQuery.select(inviteRoot);
        criteriaQuery.where(builder.and(builder.equal(inviteRoot.get("group_id"), group.getId())),
                                       (builder.equal(inviteRoot.get("sent_to_email"), user.getEmail())));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void delete(Invite invite) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(invite);
    }
}
