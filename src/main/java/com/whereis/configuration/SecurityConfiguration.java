package com.whereis.configuration;

import com.whereis.authentication.AuthenticationTokenFilter;
import com.whereis.authentication.AuthenticationTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@ComponentScan(basePackages = {"com.whereis"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    AuthenticationTokenProvider tokenAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class)
                .antMatcher("/api/*")
                .authenticationProvider(tokenAuthenticationProvider)
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/", "/api/config.json");
    }

    @Bean
    public AuthenticationTokenFilter filter() {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public AuthenticationTokenProvider provider() {
        return new AuthenticationTokenProvider();
    }
}
