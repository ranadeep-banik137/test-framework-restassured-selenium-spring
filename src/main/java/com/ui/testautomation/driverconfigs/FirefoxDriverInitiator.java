package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;

@Component(value = "firefox")
public class FirefoxDriverInitiator implements DriverFactory {
	
	private static final Logger LOGGER = Logger.getLogger(FirefoxDriverInitiator.class);
	
	private DesiredCapabilities capabilities;
	private FirefoxProfile profile;

	{
		setValue("webdriver.gecko.driver", getValue(USER_DIR) + "\\src\\main\\resources\\drivers\\geckodriver.exe");
		setDefaultBrowserSettings();
	}
	
	public DesiredCapabilities getCapabilities() {
		return this.capabilities;
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

	public FirefoxProfile getProfile() {
		return profile;
	}

	public void setProfile(FirefoxProfile profile) {
		this.profile = profile;
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
		setProfile(new FirefoxProfile());
		this.profile.addExtension(new File(getValue(USER_DIR) + "/src/main/resources/drivers/firebug-2.0.19.xpi"));
		this.profile.addExtension(new File(getValue(USER_DIR) + "/src/main/resources/drivers/firepath-0.9.7.1-fx.xpi"));
		this.profile.setPreference("javascript.enabled", true);
		this.profile.setAcceptUntrustedCertificates(true);
		this.profile.setAssumeUntrustedCertificateIssuer(true);
		this.capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		LOGGER.info("Default properties/capabilities initiated for FIREFOX driver");
	}


	@SuppressWarnings("deprecation")
	@Override
	public WebDriver initiateDriver() {
		LOGGER.info("Starting MOZILLA FIREFOX Driver....");
		return new FirefoxDriver(GeckoDriverService.createDefaultService(), getCapabilities());
	}

}
