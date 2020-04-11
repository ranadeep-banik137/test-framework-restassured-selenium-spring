package com.app.testautomation.initiators;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component(value = "webDriver")
public class Driver {

private WebDriver driver;
	
	public Driver(WebdriverInitiator initiator) {
		this.driver = initiator.initiate().startDriver().setImplicitTimeLimit(30, TimeUnit.SECONDS).maximizeWindowAtStart().getDriver();
	}

	public WebDriver getDriver() {
		return driver;
	}
}
