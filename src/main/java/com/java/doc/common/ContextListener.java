package com.java.doc.common;

import java.io.File;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebListener("application context listener")
public class ContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		/*System.out.println("The application started");
		ServletContext context = event.getServletContext();
		String log4jConfigFile = context.getInitParameter("log4j-config-location");
		String fullPath = context.getRealPath("") + File.separator;
		PropertyConfigurator.configure(fullPath);*/
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("The application stopped");
	}
}
