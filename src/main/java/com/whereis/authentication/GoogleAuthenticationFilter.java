package com.whereis.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.Filter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class GoogleAuthenticationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class.getName());

    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.info("Init GoogleAuthenticationFilter");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            Map<String,String[]> params = req.getParameterMap();
            BufferedReader bodyReader = req.getReader();
            String code = null;
            try {
                 code = bodyReader.readLine();
            } catch (IOException exception) {
                logger.info("Request body is empty, there is no security code in it");
            }
            if (!params.isEmpty() && params.containsKey("unique_visitor_code")) {
                String unique_visitor_code = params.get("unique_visitor_code")[0];
                if (code != null) {
                    Authentication auth = new GoogleAuthentication(code, unique_visitor_code);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        fc.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
