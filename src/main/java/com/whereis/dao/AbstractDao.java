package com.whereis.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public abstract class AbstractDao<T> {

    private final Class<T> persistentClass;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) (this.getClass().getGenericSuperclass().getClass());
    }


    /**
    *   Service methods
    */

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    protected CriteriaQuery createEntityCriteria(){
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        return criteriaBuilder.createQuery(persistentClass);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    //TODO: uncomment if needed else delete
    /*public void persist(T entity) {
        getSession().persist(entity);
    }*/


    /**
     *   Next methods are common for all DAO's
     */

    public T get(int id) {
        T t = entityManager.find(persistentClass, id);
        return t;
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }
}