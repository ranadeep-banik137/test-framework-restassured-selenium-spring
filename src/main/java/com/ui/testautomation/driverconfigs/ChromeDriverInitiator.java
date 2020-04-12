package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;

@Component(value = "chrome")
public class ChromeDriverInitiator implements DriverFactory {

	private static final Logger LOGGER = Logger.getLogger(ChromeDriverInitiator.class);
	private ChromeOptions options;
	private DesiredCapabilities capabilities;

	{
		setValue("webdriver.chrome.driver", getValue(USER_DIR) + "\\src\\main\\resources\\drivers\\chromedriver.exe");
		setDefaultBrowserSettings();
	}

	public ChromeOptions getOptions() {
		return options;
	}

	public void setOptions(ChromeOptions options) {
		this.options = options;
	}

	public DesiredCapabilities getCapabilities() {
		return capabilities;
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
	
	@Override
	public void setDefaultBrowserSettings() {
		setCapabilities(new DesiredCapabilities());
		setOptions(new ChromeOptions());
		this.options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		this.options.addArguments("start-maximized");
		this.options.addArguments("enable-automation"); 
		this.options.addArguments("--no-sandbox"); 
		this.options.addArguments("--disable-infobars"); 
		this.options.addArguments("--disable-dev-shm-usage"); 
		this.options.addArguments("--disable-browser-side-navigation"); 
		this.options.addArguments("--disable-gpu");
		this.capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		this.capabilities.setJavascriptEnabled(true);
		this.capabilities.setAcceptInsecureCerts(true);
		this.options.merge(getCapabilities());
		LOGGER.info("Default properties/capabilities initiated for CHROME driver");
	}

	@Override
	public WebDriver initiateDriver() {
		LOGGER.info("Starting GOOGLE CHROME Driver....");
		return new ChromeDriver(ChromeDriverService.createDefaultService(), getOptions());
	}
	
}
