package com.app.testautomation.initiators;

import static com.app.testautomation.initiators.SystemVariables.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.app.testautomation.configurations.Log4jConfiguration;
import com.app.testautomation.utilities.Assertions;
import com.app.testautomation.utilities.FileUtils;
import com.app.testautomation.utilities.ImageConverterUtility;
import com.ui.testautomation.pageobjectmodels.Covid19IndiaDashboard;
import com.ui.testautomation.pageobjectmodels.Covid19IndiaDatabaseSheet;

public class BaseSteps {

	private static final Logger LOGGER = Logger.getLogger(BaseSteps.class);

	private ApplicationContextInitiator init;
	private Covid19IndiaDashboard covid19Dashboard;
	private Covid19IndiaDatabaseSheet covid19Datasheet;
	private Driver driver;
	private WebDriver webDriver;
	private ImageConverterUtility converter;
	private Assertions assertion;
	@SuppressWarnings("unused")
	private Log4jConfiguration log4j;
		
	protected BaseSteps() {
		LOGGER.info("Base steps initiated");
		construct();
	}

	protected Covid19IndiaDashboard getCovid19Dashboard() {
		return covid19Dashboard;
	}

	protected Covid19IndiaDatabaseSheet getCovid19Datasheet() {
		return covid19Datasheet;
	}
	
	protected WebDriver getDriver() {
		return this.webDriver;
	}
	
	protected void initiateDriver() {
		if (!driver.isDriverInitiated()) {
			construct();
		}
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	private void construct() {
		FileUtils.flushFilesFromClassPath("src/test/resources/" + getValue("api") + "/shots");
		this.init = ApplicationContextInitiator.getDefaultApplicationContextInitiator();
		this.covid19Dashboard = (Covid19IndiaDashboard) this.init.getContext().getBean("covid19Dashboard");
		this.covid19Datasheet = (Covid19IndiaDatabaseSheet) this.init.getContext().getBean("covid19Datasheet");
		this.driver = (Driver) this.init.getContext().getBean("webDriver");
		this.log4j = (Log4jConfiguration) this.init.getContext().getBean("log4j");
		this.converter = (ImageConverterUtility) this.init.getContext().getBean("converter");
		this.assertion = (Assertions) this.init.getContext().getBean("assert");
		setWebDriver(this.driver.getDriver());
		setDefaultConverter();
	}

	protected ImageConverterUtility getConverter() {
		return converter;
	}
	
	private void setDefaultConverter() {
		this.converter.setHeightResolution(720);
		this.converter.setWidthResolution(1280);
		this.converter.setImagesLocation(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/shots");
		this.converter.setVideoLocation(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/videos");
		this.converter.setVideoFileName(getValue("api") + "UITest");
	}

	protected Assertions Assert() {
		return assertion;
	}
}
