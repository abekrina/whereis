package com.whereis.configuration;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.authentication.GoogleAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    GoogleAuthenticationFilter authenticationTokenFilter;

    @Autowired
    GoogleAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class)
                .antMatcher("/api/*")
                .authenticationProvider(tokenAuthenticationProvider)
                .authorizeRequests()
                .anyRequest().authenticated()
        .and()
                .csrf()
                //TODO: Enable this
                .disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/", "/api/config.json");
    }

    @Bean
    public GoogleAuthenticationFilter filter() {
        return new GoogleAuthenticationFilter();
    }
}

//TODO: выяснить можно ли разделить пользователей по ролям чтобы хендлить админство в группе