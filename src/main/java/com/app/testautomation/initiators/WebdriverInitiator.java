package com.app.testautomation.initiators;


import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.testautomation.factory.DriverFactory;

@Component(value = "initiate")
public class WebdriverInitiator {

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
		return this;
	}

	public WebdriverInitiator startDriver() {
		setDriver(getDriverFactory().initiateDriver());
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
		return this;
	}
	
	public WebdriverInitiator maximizeWindowAtStart() {
		this.driver.manage().window().maximize();
		return this;
	}
}
