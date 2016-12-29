package com.whereis.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class AuthenticationTokenFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AuthenticationTokenFilter.class.getName());

    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.info("Init AuthenticationTokenFilter");
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
            if (!params.isEmpty() && params.containsKey("state")) {
                String state = params.get("state")[0];
                if (code != null) {
                    Authentication auth = new TokenAuthentication(code, state);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        fc.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
