package com.whereis.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

public class TokenVerifyFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(TokenVerifyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init TokenVerifyFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.error("Token verify filter");
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() instanceof GoogleAuthentication) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("TokenVerifyFilter have been turned off");
    }
}
