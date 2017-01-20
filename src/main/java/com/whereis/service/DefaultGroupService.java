package com.whereis.service;

import com.whereis.dao.DefaultGroupDao;
import com.whereis.dao.GroupDao;
import com.whereis.exceptions.GroupWithIdentityExists;
import com.whereis.exceptions.NoSuchGroup;
import com.whereis.model.Group;
import com.whereis.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
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
        SecureRandom random = new SecureRandom();
        //TODO: Make identity shorter and document its size
        String token = new BigInteger(130, 2, random).toString(32);
        group.setIdentity(token);

        try {
            dao.save(group);
        } catch (GroupWithIdentityExists groupWithIdentityExists) {
            logger.error("Error during creation of group " + group.toString() + " identity exists");
            throw new DataIntegrityViolationException("Error during creation of group " + group.toString(), groupWithIdentityExists);
        }
    }

    @Override
    public void update(Group group) throws NoSuchGroup {
        if (get(group.getId()) != null) {
            dao.update(group);
        } else {
            throw new NoSuchGroup(group.toString());
        }

    }

    @Override
    public void delete(Group group) {
        dao.delete(group);
    }

    @Override
    public Group getByIdentity(String identity) {
        return dao.getByIdentity(identity);
    }
}
