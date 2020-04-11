package com.app.testautomation.initiators;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "webDriver")
public class Driver {

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
		}
	}
	
	public boolean isDriverInitiated() {
		Boolean flag = true;
		String windowHandle = null;
		try {
			windowHandle = getDriver().getWindowHandle();
		} catch(NoSuchSessionException session) {
			windowHandle = null;
			System.out.println(session.getMessage());
		} catch (NullPointerException exception) {
			windowHandle = null;
			System.out.println(exception.getMessage());
		}
		if (windowHandle == null) {
			flag = false;
		}
		return flag;
	}
}
