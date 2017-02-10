package com.whereis.dao;

import com.whereis.exceptions.invites.UserAlreadyInvited;
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
@Repository("inviteDao")
public class DefaultInviteDao extends  AbstractDao<Invite> implements InviteDao {

    @Override
    public void save(Invite invite) throws UserAlreadyInvited {
        if (getSameInvite(invite) == null) {
            getSession().persist(invite);
        } else {
            throw new UserAlreadyInvited("Invite for user " + invite.getSentToUser()
                    + " to group " + invite.getGroup() + " already exists");
        }
    }

    @Override
    public void update(Invite invite) {
        if (get(invite.getId()) != null) {
            getSession().update(invite);
        }
    }

    @Override
    public Invite getSameInvite(Invite invite) {
        CriteriaBuilder builder = getCriteriaBuilder();
        @SuppressWarnings("unchecked")
        CriteriaQuery<Invite> criteriaQuery = createEntityCriteria();
        Root<Invite> inviteRoot = criteriaQuery.from(Invite.class);
        criteriaQuery.select(inviteRoot);
        criteriaQuery.where(builder.and(builder.equal(inviteRoot.get("sent_to_user"), invite.getSentToUser())),
                builder.equal(inviteRoot.get("group"), invite.getGroup()));
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
        pendingInvite.setGroup(group);
        pendingInvite.setSentToUser(user);

        return getSameInvite(pendingInvite);
    }
}
