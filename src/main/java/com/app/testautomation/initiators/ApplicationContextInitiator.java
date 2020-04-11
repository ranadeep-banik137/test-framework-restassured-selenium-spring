package com.app.testautomation.initiators;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ApplicationContextInitiator {
	
	private WebdriverInitiator factory;
	private ApplicationContext context;

	private ApplicationContextInitiator() {
		setInProjectContext();
		//setDefaultFactory();
	}

	
	public ApplicationContext getContext() {
		return context;
	}
	
	private void setInProjectContext() {
		this.context = new AnnotationConfigApplicationContext(ContextInitiators.class);
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	public static ApplicationContextInitiator getDefaultApplicationContextInitiator() {
		return new ApplicationContextInitiator();
	}

	public WebdriverInitiator startFactory() {
		return factory;
	}
	
	public void setDefaultFactory() {
		this.factory = (WebdriverInitiator) this.context.getBean("initiate");
	}

	public void setFactory(WebdriverInitiator factory) {
		this.factory = factory;
	}
}
