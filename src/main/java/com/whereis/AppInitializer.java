package com.whereis;

import com.whereis.configuration.LogConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

public class AppInitializer implements WebApplicationInitializer {
	private static final String CONFIG_LOCATION = "com.whereis.configuration";
	private static final String MAPPING_URL = "/api/*";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());

		WebApplicationContext context = getContext();
		servletContext.addListener(new RequestContextListener());
		servletContext.addListener(new ContextLoaderListener(context));
		Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(MAPPING_URL);
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(CONFIG_LOCATION);
		return context;
	}

}