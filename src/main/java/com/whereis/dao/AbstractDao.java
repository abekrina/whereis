package com.whereis.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public abstract class AbstractDao<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) (this.getClass().getGenericSuperclass().getClass());
    }

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    EntityManager entityManager;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public void persist(T entity) {
        getSession().persist(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public T findById(int id) {
        T t = entityManager.find(persistentClass, id);
        return t;
    }

    protected CriteriaQuery createEntityCriteria(){
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        return criteriaBuilder.createQuery(persistentClass);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

}