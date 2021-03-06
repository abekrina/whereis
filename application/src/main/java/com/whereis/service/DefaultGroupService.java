package com.whereis.service;

import com.whereis.dao.DefaultGroupDao;
import com.whereis.dao.GroupDao;
import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoSuchGroupException;
import com.whereis.model.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("groupService")
@Transactional
public class DefaultGroupService implements GroupService {
    private static final Logger logger = LogManager.getLogger(DefaultGroupDao.class);
    @Autowired
    GroupDao dao;

    @Override
    public Group get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(Group group) {
        try {
            dao.save(group);
        } catch (GroupWithIdentityExistsException groupWithIdentityExistsException) {
            logger.error("Error during creation of group " + group.toString() + " identity exists");
            throw new DataIntegrityViolationException("Error during creation of group " + group.toString(), groupWithIdentityExistsException);
        }
    }

    @Override
    public void update(Group group) throws NoSuchGroupException {
        if (get(group.getId()) != null) {
            dao.update(group);
        } else {
            throw new NoSuchGroupException(group.toString());
        }

    }

    @Override
    public boolean delete(Group group) {
        return dao.delete(group.getClass(), group.getId());
    }

    @Override
    public Group getByIdentity(String identity) {
        return dao.getByIdentity(identity);
    }
}
