package com.whereis.service;

import com.whereis.exceptions.UserAlreadyInvited;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;

public interface InviteService {
    Invite get(int id);
    void save(Invite invite) throws UserAlreadyInvited;
    void update(Invite invite);
    boolean delete(Invite invite);
    Invite getSameInvite(Invite invite);
    Invite getPendingInviteFor(User user, Group group);
}
