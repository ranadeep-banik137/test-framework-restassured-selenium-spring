package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.getValue;
import static com.app.testautomation.initiators.SystemVariables.setValue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaDriverService;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;

@Component(value = "opera")
public class OperaDriverInitiator implements DriverFactory {

	private DesiredCapabilities capabilities;
	private OperaOptions options;

	{
		setValue("webdriver.opera.driver", getValue("user.dir") + "\\src\\main\\resources\\drivers\\operadriver.exe");
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
	
	public DesiredCapabilities getCapabilities() {
		return this.capabilities;
		
	}

	@Override
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) throws BrowserCapabilitiesNotInitiatedException {
		if (this.capabilities == null) {
			throw new BrowserCapabilitiesNotInitiatedException();
		}
		this.capabilities.merge(capabilities);
		return this;
	}

	public OperaOptions getOptions() {
		return options;
	}

	public void setOptions(OperaOptions options) {
		this.options = options;
	}

	@Override
	public void setDefaultBrowserSettings() {
		setCapabilities(new DesiredCapabilities());
		setOptions(new OperaOptions());
		this.capabilities.setJavascriptEnabled(true);
		this.capabilities.setAcceptInsecureCerts(true);
		this.options.merge(getCapabilities());
	}

	@Override
	public WebDriver initiateDriver() {
		return new OperaDriver(OperaDriverService.createDefaultService(), getOptions());
	}

}
