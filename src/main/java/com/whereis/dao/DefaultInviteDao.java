package com.whereis.dao;

import com.whereis.exceptions.UserAlreadyInvited;
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
    public void save(Invite invite) throws UserAlreadyInvited {
        if (getSameInvite(invite) == null) {
            if (invite.getTimestamp() == null) {
                invite.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.persist(invite);
        } else {
            throw new UserAlreadyInvited("Invite for email " + invite.getSentToEmail()
                    + " to group " + invite.getGroupId() + " already exists");
        }
    }

    @Override
    public void update(Invite invite) {
        if (get(invite.getId()) != null) {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.update(invite);
        }
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
            return getSession().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    // Returns null if no invites for user and group
    @Override
    public Invite getPendingInviteFor(User user, Group group) {
        Invite pendingInvite = new Invite();
        pendingInvite.setGroupId(group.getId());
        pendingInvite.setSentToEmail(user.getEmail());

        return getSameInvite(pendingInvite);
    }
}
