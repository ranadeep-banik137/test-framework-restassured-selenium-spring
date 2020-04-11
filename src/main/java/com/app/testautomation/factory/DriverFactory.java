package com.app.testautomation.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;


@Component(value = "driverFactory")
public interface DriverFactory {
	
	public void setDefaultBrowserSettings();
	
	public DriverFactory setCapabilities(DesiredCapabilities capabilities);
	
	public DriverFactory setExternalCapabilities(String key, Object value) throws Exception;
	
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) throws Exception;
	
	public WebDriver initiateDriver();
	
	/*
	 * default WebDriver changeDriver(DriverFactory factory) { return
	 * factory.initiateDriver().getWebDriver(); }
	 */
	
}
