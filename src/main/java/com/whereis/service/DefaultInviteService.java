package com.whereis.service;

import com.whereis.dao.InviteDao;
import com.whereis.exceptions.invites.UserAlreadyInvitedException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultInviteService implements InviteService {
    @Autowired
    private InviteDao dao;

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
        return dao.delete(invite.getClass(), invite.getId());
    }

    @Override
    public Invite getSameInvite(Invite invite) {
        return dao.getSameInvite(invite);
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
    public Invite getPendingInviteFor(User user, Group group) {
        Invite pendingInvite = new Invite();
        pendingInvite.setGroup(group);
        pendingInvite.setSentToUser(user);

        return getSameInvite(pendingInvite);
    }
}
