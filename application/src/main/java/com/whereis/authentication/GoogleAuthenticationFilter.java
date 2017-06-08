package com.whereis.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

public class GoogleAuthenticationFilter implements Filter {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.info("Init GoogleAuthenticationFilter");
    }

    /**
     * Checks if there is a token in body and move request forward by filter chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse responce, FilterChain filterChain) throws IOException,
            ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        String body = request.getReader().readLine();
        if (body != null && body.length() > 1) {
            context.setAuthentication(new GoogleAuthentication(body));
            logger.error("KEKEKEKEKEKEKEKEK on login filter");
            filterChain.doFilter(request, responce);
        }
    }

    @Override
    public void destroy() {
        logger.info("GoogleAuthenticationFilter have been turned off");
    }
}
