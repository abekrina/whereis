package com.whereis.dao;

import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Repository("locationDao")
public class DefaultLocationDao extends AbstractDao<Location> implements LocationDao {

    @Override
    public void save(Location location) {
        getSession().persist(location);
    }

    @Override
    public void refresh(Location location) {
        getSession().refresh(location);
    }
}
