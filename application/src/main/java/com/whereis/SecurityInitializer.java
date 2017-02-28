package com.whereis;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(value = 1)
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
