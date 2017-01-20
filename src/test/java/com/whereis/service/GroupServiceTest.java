package com.whereis.service;

import com.whereis.dao.DefaultGroupDao;
import com.whereis.dao.GroupDao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;

public class GroupServiceTest {
    @Mock
    GroupDao dao;

    @InjectMocks
    DefaultGroupDao defaultGroupDao;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


}
