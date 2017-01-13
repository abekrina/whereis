package com.whereis.service;

import com.whereis.dao.InviteDao;
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
    public void save(Invite invite) {
        dao.save(invite);
    }

    @Override
    public void update(Invite invite) {
        dao.update(invite);
    }

    @Override
    public void delete(Invite invite) {
        dao.delete(invite);
    }

    @Override
    public Invite getSameInvite(Invite invite) {
        return dao.getSameInvite(invite);
    }

    @Override
    public Invite getPendingInviteFor(User user, Group group) {
        return dao.getPendingInviteFor(user, group);
    }

}