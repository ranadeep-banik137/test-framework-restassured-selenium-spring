package com.app.testautomation.initiators;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "webDriver")
public class Driver {

	private static final Logger LOGGER = Logger.getLogger(Driver.class);
	
	private WebDriver driver;
	private WebdriverInitiator initiator;
	
	@Autowired
	public Driver(WebdriverInitiator initiator) {
		this.initiator = initiator;
		initiateDriver();
	}

	public WebDriver getDriver() {
		return driver;
	}
	
	public void initiateDriver() {
		if (!isDriverInitiated()) {
			this.driver = initiator.initiate().startDriver().setImplicitTimeLimit(30, TimeUnit.SECONDS).maximizeWindowAtStart().getDriver();
			LOGGER.info("WebDriver initiated");
		}
	}
	
	public boolean isDriverInitiated() {
		Boolean flag = true;
		String windowHandle = null;
		try {
			windowHandle = getDriver().getWindowHandle();
			LOGGER.info("Driver session is active");
		} catch(NoSuchSessionException session) {
			windowHandle = null;
			LOGGER.info("Driver session is not active");
			LOGGER.info(session.getMessage());
		} catch (NullPointerException exception) {
			windowHandle = null;
			LOGGER.info("Driver instance is null");
		}
		if (windowHandle == null) {
			flag = false;
		}
		return flag;
	}
}
