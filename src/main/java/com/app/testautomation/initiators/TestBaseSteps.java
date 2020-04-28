package com.app.testautomation.initiators;

import static com.app.testautomation.initiators.SystemVariables.USER_DIR;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;

import com.app.testautomation.configurations.Log4jConfiguration;
import com.app.testautomation.utilities.Assertions;
import com.app.testautomation.utilities.FileUtils;
import com.app.testautomation.utilities.ImageConverterUtility;

public class TestBaseSteps {

	private static final Logger LOGGER = Logger.getLogger(TestBaseSteps.class);

	private ApplicationContext context;
	private Driver driver;
	private WebDriver webDriver;
	private ImageConverterUtility converter;
	private Assertions assertion;
	@SuppressWarnings("unused")
	private Log4jConfiguration log4j;
	private RestCall restCall;
		
	protected TestBaseSteps() {
		LOGGER.info("Base steps initiated");
		construct();
	}

	protected WebDriver getDriver() {
		return this.webDriver;
	}
	
	protected Driver getDriverInitiator() {
		return this.driver;
	}
	
	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	protected void construct() {
		FileUtils.flushFilesFromClassPath("src/test/resources/" + getValue("api") + "/shots");
		this.context = ApplicationContextInitiator.getDefaultApplicationContextInitiator().getContext();
		this.driver = (Driver) this.getContext().getBean("webDriver");
		this.log4j = (Log4jConfiguration) this.getContext().getBean("log4j");
		this.converter = (ImageConverterUtility) this.getContext().getBean("converter");
		this.assertion = (Assertions) this.getContext().getBean("assert");
		this.restCall = (RestCall) this.getContext().getBean("rest");
		setWebDriver(this.driver.getDriver());
		setDefaultConverter();
	}

	private void setDefaultConverter() {
		this.converter.setHeightResolution(720);
		this.converter.setWidthResolution(1280);
		this.converter.setImagesLocation(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/shots");
		this.converter.setVideoLocation(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/videos");
		this.converter.setVideoFileName(getValue("api") + "UITest");
	}

	protected Assertions Assert() {
		return assertion;
	}

	protected RestCall restCall() {
		return restCall;
	}

	protected ApplicationContext getContext() {
		return context;
	}
	
	protected ImageConverterUtility getConverter() {
		return converter;
	}
}
