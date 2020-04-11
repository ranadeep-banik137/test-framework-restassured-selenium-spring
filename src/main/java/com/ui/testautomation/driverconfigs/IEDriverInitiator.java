package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.getValue;
import static com.app.testautomation.initiators.SystemVariables.setValue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;

@Component(value = "ie")
public class IEDriverInitiator implements DriverFactory {

	private DesiredCapabilities capabilities;
	private InternetExplorerOptions options;
	
	
	{
		setValue("webdriver.ie.driver", getValue("user.dir") + "\\src\\main\\resources\\drivers\\IEDriverServer.exe");
		setDefaultBrowserSettings();
	}

	@Override
	public DriverFactory setCapabilities(DesiredCapabilities capabilities) {
		this.capabilities = capabilities;
		return this;
	}

	@Override
	public DriverFactory setExternalCapabilities(String key, Object value) throws BrowserCapabilitiesNotInitiatedException {
		if (this.capabilities == null) {
			throw new BrowserCapabilitiesNotInitiatedException();
		}
		this.capabilities.setCapability(key, value);
		return this;
	}

	@Override
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) throws BrowserCapabilitiesNotInitiatedException {
		if (this.capabilities == null) {
			throw new BrowserCapabilitiesNotInitiatedException();
		}
		this.capabilities.merge(capabilities);
		return this;
	}

	public InternetExplorerOptions getOptions() {
		return options;
	}

	public void setOptions(InternetExplorerOptions options) {
		this.options = options;
	}
	
	public DesiredCapabilities getCapabilities() {
		return this.capabilities;
	}

	@Override
	public void setDefaultBrowserSettings() {
		setCapabilities(new DesiredCapabilities());
		setOptions(new InternetExplorerOptions());
		this.capabilities.setCapability("ignoreZoomSetting", true);
		this.capabilities.setJavascriptEnabled(true);
		//this.capabilities.setAcceptInsecureCerts(true);
		this.options.destructivelyEnsureCleanSession();
		this.options.merge(getCapabilities());
		
	}

	@Override
	public WebDriver initiateDriver() {
		return new InternetExplorerDriver(InternetExplorerDriverService.createDefaultService(), getOptions());
	}

}
