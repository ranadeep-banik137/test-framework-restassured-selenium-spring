package com.ui.testautomation.driverconfigs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;
import org.testng.log4testng.Logger;

import com.app.testautomation.factory.DriverFactory;

@Component(value = "safari")
public class SafariDriverInitiator implements DriverFactory {
	
	private static final Logger LOGGER = Logger.getLogger(SafariDriverInitiator.class);

	{
		//This class wouldn't execute as Windows dont have any access to safari.
		//Try Browserstack
	}
	
	@SuppressWarnings("unused")
	private WebDriver driver;
	@SuppressWarnings("unused")
	private DesiredCapabilities capabilities;

	@Override
	public DriverFactory setCapabilities(DesiredCapabilities capabilities) {
		return null;
	}

	@Override
	public DriverFactory setExternalCapabilities(String key, Object value) {
		return null;
	}

	@Override
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) {
		return null;
	}

	@Override
	public void setDefaultBrowserSettings() {
		LOGGER.info("Browser selected : SAFARI");
		LOGGER.info("**NOTE : SAFARI Browser is not supported in windows, hence execution not possible");
		
	}

	@Override
	public WebDriver initiateDriver() {
		LOGGER.info("Browser SAFARI not supported to run in windows");
		LOGGER.error("Browser SAFARI not supported to run in windows");
		System.exit(-1);
		return null;
	}

}
