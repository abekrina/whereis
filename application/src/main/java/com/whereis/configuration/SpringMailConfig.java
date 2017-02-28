/*
package com.whereis.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:resources/javamail.properties")
public class SpringMailConfig implements ApplicationContextAware, EnvironmentAware {

    private static final String JAVA_MAIL_FILE = "classpath:resources/javamail.properties";

    private ApplicationContext applicationContext;
    private Environment environment;

    @Bean
    public JavaMailSender mailSender() throws IOException {

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Basic mail sender configuration, based on emailconfig.properties
        mailSender.setHost(this.environment.getProperty("javamail.host"));
        mailSender.setPort(Integer.parseInt(this.environment.getProperty("javamail.port")));
        mailSender.setProtocol(this.environment.getProperty("javamail.protocol"));
        mailSender.setUsername(this.environment.getProperty("javamail.username"));
        mailSender.setPassword(this.environment.getProperty("javamail.password"));

        // JavaMail-specific mail sender configuration, based on javamail.properties
        final Properties javaMailProperties = new Properties();
        javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}*/
