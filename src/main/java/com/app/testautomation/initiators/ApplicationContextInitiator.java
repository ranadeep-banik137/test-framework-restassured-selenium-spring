package com.app.testautomation.initiators;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ApplicationContextInitiator {
	
	private static final Logger LOGGER = Logger.getLogger(ApplicationContextInitiator.class);
	private ApplicationContext context;

	private ApplicationContextInitiator() {
		setInProjectContext();
	}

	
	public ApplicationContext getContext() {
		return context;
	}
	
	private void setInProjectContext() {
		LOGGER.info("Application context created and initiated all the beans in Spring bean container");
		this.context = new AnnotationConfigApplicationContext(ContextInitiators.class);
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	public static ApplicationContextInitiator getDefaultApplicationContextInitiator() {
		LOGGER.info("return new ApplicationContextInitiator()");
		return new ApplicationContextInitiator();
	}

}
