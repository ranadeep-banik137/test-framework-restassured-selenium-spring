package com.app.testautomation.initiators;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "webDriver")
public class Driver {

	private static final Logger LOGGER = Logger.getLogger(Driver.class);
	
	private static int screenShotCount = 0;
	private WebDriver driver;
	private WebdriverInitiator initiator;
	private TakesScreenshot screenshotTaker;
	
	@Autowired
	public Driver(WebdriverInitiator initiator) {
		this.initiator = initiator;
		initiateDriver();
	}

	public WebDriver getDriver() {
		return driver;
	}
	
	public void initiateDriver() {
		if (!isDriverInitiated()) {
			this.driver = initiator.initiate().startDriver().setImplicitTimeLimit(30, TimeUnit.SECONDS).maximizeWindowAtStart().getDriver();
			setScreenshotTaker(this.initiator.getScreenshotTaker());
			LOGGER.info("WebDriver initiated");
		}
	}
	
	public boolean isDriverInitiated() {
		Boolean flag = true;
		String windowHandle = null;
		try {
			windowHandle = getDriver().getWindowHandle();
			LOGGER.info("Driver session is active");
		} catch(NoSuchSessionException session) {
			windowHandle = null;
			LOGGER.info("Driver session is not active");
			LOGGER.info(session.getMessage());
		} catch (NullPointerException exception) {
			windowHandle = null;
			LOGGER.info("Driver instance is null");
		}
		if (windowHandle == null) {
			flag = false;
		}
		return flag;
	}
	
	
	public void takeScreenShot(String pageSource) {
		try {
			File screenShot = this.screenshotTaker.getScreenshotAs(OutputType.FILE);
			String screenShotName =  "RDB_" + (screenShotCount < 10 ? "0" : "") + screenShotCount + "_" + getValue("api") + "_" + pageSource + ".jpg";
			FileHandler.copy(screenShot, new File(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/shots/" + screenShotName));
			screenShotCount++;
			LOGGER.info("Screen shot captured at " + pageSource + " page");
			LOGGER.info("Find the screen shot at : src/test/resources/shots/" + screenShotName);
		} catch (IOException exception) {
			LOGGER.info(exception.getMessage());
		}
	}

	public TakesScreenshot getScreenshotTaker() {
		return screenshotTaker;
	}

	public void setScreenshotTaker(TakesScreenshot screenshotTaker) {
		this.screenshotTaker = screenshotTaker;
	}
}
