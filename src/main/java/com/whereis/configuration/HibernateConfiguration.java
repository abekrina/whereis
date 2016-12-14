package com.whereis.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.sql.DataSource;
import javax.transaction.xa.XAResource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jca.support.LocalConnectionFactoryBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.whereis.models")
@PropertySource("classpath:application.properties")
public class HibernateConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.whereis.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public LocalConnectionFactoryBean connectionFactory() {
        LocalConnectionFactoryBean connectionFactory = new LocalConnectionFactoryBean();
        connectionFactory.setManagedConnectionFactory(new ManagedConnectionFactory() {
            @Override
            public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException {
                return null;
            }

            @Override
            public Object createConnectionFactory() throws ResourceException {
                return null;
            }

            @Override
            public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
                return null;
            }

            @Override
            public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter printWriter) throws ResourceException {

            }

            @Override
            public PrintWriter getLogWriter() throws ResourceException {
                return null;
            }
        });
        return connectionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));

        return properties;
    }

    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);

        return transactionManager;
    }
}