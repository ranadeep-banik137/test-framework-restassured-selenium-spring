package com.ui.testautomation.driverconfigs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.app.testautomation.factory.DriverFactory;

@Component(value = "safari")
public class SafariDriverInitiator implements DriverFactory {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DriverFactory setExternalCapabilities(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultBrowserSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WebDriver initiateDriver() {
		// TODO Auto-generated method stub
		return null;
	}

}
