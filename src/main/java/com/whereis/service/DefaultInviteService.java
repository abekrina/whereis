package com.whereis.service;

import com.whereis.dao.GroupDao;
import com.whereis.dao.InviteDao;
import com.whereis.dao.UserDao;
import com.whereis.exceptions.invites.UserAlreadyInvitedException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;
import java.util.Set;

@Service
public class DefaultInviteService implements InviteService {
    @Autowired
    private InviteDao dao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Override
    public Invite get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(Invite invite) throws UserAlreadyInvitedException {
        dao.save(invite);
    }

    @Override
    public void update(Invite invite) {
        dao.update(invite);
    }

    @Override
    public boolean delete(Invite invite) {
        invite.getSentToUser().deleteInviteForUser(invite);
        invite.getGroup().deleteInviteToGroup(invite);
        return dao.delete(invite.getClass(), invite.getId());
    }

    @Override
    public boolean haveInvitesForUser(User user, Group group){
        if (getPendingInviteFor(user, group) != null) {
            return true;
        }
        return false;
    }

    /**
     * Find invite for user to group if exists
     * @param user user to search invite for
     * @param group group to search invite for
     * @return invite for user to group or null if not found
     */
    @Override
    @Transactional
    public Invite getPendingInviteFor(User user, Group group) {
        user = userDao.get(user.getId());
        Set<Invite> inviteSet = user.getUserInvites();
        for (Invite invite : inviteSet) {
            if (invite.getGroup().equals(group)) {
                return invite;
            }
        }
        return null;
    }

    @Override
    public void saveInviteForUser(Invite invite) throws UserAlreadyInvitedException {
        save(invite);
        invite.getSentToUser().addInviteForUser(invite);
        invite.getGroup().addInviteToGroup(invite);
    }
}
