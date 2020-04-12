package com.app.testautomation.initiators;


import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.testautomation.factory.DriverFactory;

@Component(value = "initiate")
public class WebdriverInitiator {

	private static final Logger LOGGER = Logger.getLogger(WebdriverInitiator.class);
	
	private WebDriver driver;
	
	private DriverFactory factory;
	
	@Autowired
	private DriverFactory firefox;
	
	@Autowired
	private DriverFactory chrome;
		
	@Autowired
	private DriverFactory ie;
	
	@Autowired
	private DriverFactory opera;
	
	@Autowired
	private DriverFactory safari;
	
	@Autowired
	private DriverFactory remote;
	
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebdriverInitiator initiate() {
		setDriverBasedOnDriverName(getValue(SystemVariables.BROWSER));
		LOGGER.info("Browser key provided : " + getValue(SystemVariables.BROWSER));
		return this;
	}

	public WebdriverInitiator startDriver() {
		setDriver(getDriverFactory().initiateDriver());
		LOGGER.info(getValue(SystemVariables.BROWSER) + " browser initiated");
		return this;
	}
	
	public WebdriverInitiator modifyBrowserCapabilities(DesiredCapabilities capabilities) throws Exception {
		setDriverFactory(getDriverFactory().setExternalCapabilities(capabilities));
		return this;
	}
	
	public WebdriverInitiator replaceAllCapabilities(DesiredCapabilities capabilities) {
		setDriverFactory(getDriverFactory().setCapabilities(capabilities));
		return this;
	}

	public DriverFactory getDriverFactory() {
		return factory;
	}

	public void setDriverFactory(DriverFactory factory) {
		this.factory = factory;
	}
	
	public WebdriverInitiator changeDriver(String driver) {
		setDriverBasedOnDriverName(driver);
		LOGGER.info("Change driver requested. Driver : " + driver.toUpperCase());
		return this;
	}
	
	private void setDriverBasedOnDriverName(String driver) {
		if (Boolean.getBoolean(SystemVariables.GRID_RUN) || Boolean.getBoolean(SystemVariables.BROWSERSTACK_RUN)) {
			setDriverFactory(remote);
		} else {
			switch(driver) {
			
			case "chrome" : {
				setDriverFactory(chrome);
				break;
			}
			
			case "firefox" : {
				setDriverFactory(firefox);
				break;
			}
			
			case "ie" : {
				setDriverFactory(ie);
				break;
			}
			
			case "opera" : {
				setDriverFactory(opera);
				break;
			}
			
			case "safari" : {
				setDriverFactory(safari);
				break;
			}
			
			default :
				//No Driver set in default
			}
		}
	}
	
	public WebdriverInitiator setImplicitTimeLimit(int time, TimeUnit unit) {
		this.driver.manage().timeouts().implicitlyWait(time, unit);
		LOGGER.info("Driver implicit wait initiated for " + time + " " + unit);
		return this;
	}
	
	public WebdriverInitiator maximizeWindowAtStart() {
		this.driver.manage().window().maximize();
		LOGGER.info("Browser window maximized");
		return this;
	}
}
