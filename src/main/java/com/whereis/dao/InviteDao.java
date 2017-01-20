package com.whereis.dao;

import com.whereis.exceptions.UserAlreadyInvited;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;

public interface InviteDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    Invite get(int id);
    void delete(Invite invite);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Invite invite) throws UserAlreadyInvited;
    void update(Invite invite);
    Invite getSameInvite(Invite invite);
    Invite getPendingInviteFor(User user, Group group);
}
