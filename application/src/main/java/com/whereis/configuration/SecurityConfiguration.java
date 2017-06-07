package com.whereis.configuration;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.authentication.GoogleAuthenticationProvider;
import com.whereis.authentication.TokenVerifyFilter;
import com.whereis.authentication.TokenVerifyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    GoogleAuthenticationFilter googleAuthenticationFilter;
    @Autowired
    GoogleAuthenticationProvider googleAuthenticationProvider;

    @Autowired
    TokenVerifyFilter tokenVerifyFilter;
    @Autowired
    TokenVerifyProvider tokenVerifyProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(googleAuthenticationFilter, BasicAuthenticationFilter.class)
                .antMatcher("/login")
                .authenticationProvider(googleAuthenticationProvider)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(tokenVerifyFilter, RememberMeAuthenticationFilter.class)
                .antMatcher("/api/**")
                .authenticationProvider(tokenVerifyProvider)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean
    public GoogleAuthenticationFilter filter() {
        return new GoogleAuthenticationFilter();
    }

    @Bean
    public TokenVerifyFilter tokenVerifyFilter() {
        return new TokenVerifyFilter();
    }

    @Bean
    public TokenVerifyProvider tokenVerifyProvider() {
        return new TokenVerifyProvider();
    }
}