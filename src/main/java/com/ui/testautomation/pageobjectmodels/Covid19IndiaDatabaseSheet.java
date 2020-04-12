package com.ui.testautomation.pageobjectmodels;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import com.app.testautomation.initiators.Driver;

@Component(value = "covid19Datasheet")
public class Covid19IndiaDatabaseSheet {
	
	private static final Logger LOGGER = Logger.getLogger(Covid19IndiaDatabaseSheet.class);
	private WebDriver driver;
	
	@FindBy(xpath = "//tbody/tr[contains(@style,'46px')]")
	private WebElement headerRowElement;
	
	@Autowired
	public Covid19IndiaDatabaseSheet(Driver webDriver) {
		this.driver = webDriver.getDriver();
		PageFactory.initElements(getDriver(), this);
	}

	public WebDriver getDriver() {
		return driver;
	}
	
	public void verifyPageUrl() {
		LOGGER.info("Current URL : " + getDriver().getCurrentUrl());
		Assert.assertTrue(getDriver().getCurrentUrl().equals("https://docs.google.com/spreadsheets/d/e/2PACX-1vSc_2y5N0I67wDU38DjDh35IZSIS30rQf7_NYZhtYYGU1jJYT6_kDx4YpF-qw0LSlGsBYP8pqM_a1Pd/pubhtml"));
	}
	
	public void switchToDatabaseWindow() {
		Set<String> windowHandles = getDriver().getWindowHandles();
		Iterator<String> windowIterator = windowHandles.iterator();
		String currentHandle;
		while (windowIterator.hasNext()) {
			currentHandle = windowIterator.next();
			LOGGER.info("Currently on window : " + currentHandle);
			getDriver().switchTo().window(currentHandle);
			if (getDriver().getCurrentUrl().contains("docs.google")) {
				LOGGER.info(getDriver().getCurrentUrl());
				LOGGER.info("Successfully browsed to COVID19 Datasheet page");
				break;
			}
		}
	}

}
