package com.ui.testautomation.pageobjectmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.app.testautomation.initiators.Driver;
import com.app.testautomation.utilities.WebElementModifier;

@Component(value = "covid19Dashboard")
public class Covid19IndiaDashboard {
	
	private static final Logger LOGGER = Logger.getLogger(Covid19IndiaDashboard.class);
	private static final String PAGE_SOURCE = "Covid19IndiaDashboard";
	
	private Driver webDriverInitiator;
	private WebDriver driver;
	private WebDriverWait explicitWait;
	private JavascriptExecutor executor;
	private Actions actions;
	
	@Autowired
	private WebElementModifier webElementModifier;
	
	@FindBy(xpath = "//div[@class = 'level-item is-cherry fadeInUp']/h5/following-sibling::h1")
	private WebElement noOfConfirmedCases;
	
	@FindBy(xpath = "//tr[@class='state']")
	private List<WebElement> stateRows;
	
	@FindBy(xpath = "//thead//th//abbr")
	private List<WebElement> stickyHeads;
	
	@FindBy(xpath = "//a[@class='button excel']")
	private WebElement patientDatabaseProviderButton;
	
	@FindBy(xpath = "//div[@class='navbar-right']//a[@href='/']")
	private WebElement navigateToHomeButton;
	
	@FindBy(xpath = "//div[@class='expand']//a[@href='/']")
	private WebElement navigateToExpandedHomeTab;
	
	@Autowired 
	public Covid19IndiaDashboard(Driver webDriver) { 
		this.webDriverInitiator = webDriver;
		this.driver = webDriver.getDriver();
		this.actions = new Actions(this.driver);
		PageFactory.initElements(getDriver(), this);
		this.explicitWait = new WebDriverWait(getDriver(), 15);
		this.executor = (JavascriptExecutor) getDriver();
	}
	 
	public WebDriver getDriver() { return driver; }
	 
	public int getColumnNumber(String columnName) {
		int columnNumber = 0;
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stickyHeads));
		List<String> columnNameList = new ArrayList<>();
		Iterator<WebElement> iterator = stickyHeads.listIterator();
		while (iterator.hasNext()) {
			columnNameList.add(iterator.next().getText().trim());
		}
		columnNumber = columnNameList.indexOf(columnName) + 1;
		return columnNumber;
	}
	
	public WebElement getStateRowElement(String state) {
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stateRows));
		List<WebElement> requiredWebElementList = stateRows.stream().filter(x-> webElementModifier.appendWebElement(x, By.xpath(".//div[@class='title-chevron']")).getText().trim().equalsIgnoreCase(state)).collect(Collectors.toList());
		WebElement requiredWebElement = requiredWebElementList.get(0);
		this.executor.executeScript("arguments[0].scrollIntoView(true);", requiredWebElement);
		return requiredWebElement;
	}
	
	public void browseToDashboard() {
		try {
		String url = "http://www.covid19India.org";
		getDriver().navigate().to(url);
		this.explicitWait.until(ExpectedConditions.elementToBeClickable(navigateToHomeButton));
		this.actions.moveToElement(navigateToHomeButton).perform();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		this.explicitWait.until(ExpectedConditions.visibilityOf(navigateToExpandedHomeTab));
		this.navigateToExpandedHomeTab.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		this.explicitWait.until(ExpectedConditions.visibilityOf(noOfConfirmedCases));
		LOGGER.info("Navigated to : " + url);
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
	}
	
	public int getTotalConfirmedCasesCount() {
		this.explicitWait.until(ExpectedConditions.visibilityOf(noOfConfirmedCases));
		int totalNumberOfConfirmedCases = Integer.parseInt(noOfConfirmedCases.getText().replace(",", "").trim());
		LOGGER.info("Total number of confirmed COVID19 cases in India : " + totalNumberOfConfirmedCases);
		return totalNumberOfConfirmedCases;
	}
	
	public void validateStateUICalculation(String state) {
		try {
		WebElement stateRow = getStateRowElement(state);
		String parsedElement = ".//td[%s]//span[@class='total']";
		int confirmedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Confirmed")))).getText().replace(",", ""));
		int activeCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Active")))).getText().replace(",", "").replace("-", "0"));
		int recoveredCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Recovered")))).getText().replace(",", "").replace("-", "0"));
		int deceasedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Deceased")))).getText().replace(",", "").replace("-", "0"));
		LOGGER.info("State :" + state);
		LOGGER.info("Confirmed : " + confirmedCases + ", Active : " + activeCases +", Recovered : " + recoveredCases + ", Deceased : " + deceasedCases);
		LOGGER.info("Verified Active Cases : Confirmed - Recovered + deceased");
		LOGGER.info(confirmedCases + " - " + recoveredCases + " + " + deceasedCases + " = " + (confirmedCases - recoveredCases - deceasedCases));
		Assert.assertTrue(activeCases == (confirmedCases - recoveredCases - deceasedCases), "Case calculations are not matching");
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_" + state + "_StateScenario");
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
	}
	
	public int getTotalConfirmedCountOf(String state) {
		int confirmedCases = 0;
		try {
			WebElement stateRow = getStateRowElement(state);
			String parsedElement = ".//td[%s]//span[@class='total']";
			confirmedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Confirmed")))).getText().replace(",", ""));
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
		return confirmedCases;
	}
	
	public int getTotalActiveCountOf(String state) {
		int activeCases = 0;
		try {
			WebElement stateRow = getStateRowElement(state);
			String parsedElement = ".//td[%s]//span[@class='total']";
			activeCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Active")))).getText().replace(",", "").replace("-", "0"));
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
		return activeCases;
	}
	
	
	public int getTotalDeceasedCountOf(String state) {
		int deceasedCases = 0;
		try {
			WebElement stateRow = getStateRowElement(state);
			String parsedElement = ".//td[%s]//span[@class='total']";
			deceasedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Deceased")))).getText().replace(",", "").replace("-", "0"));
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
		return deceasedCases;
	}
	
	public int getTotalRecoveredCountOf(String state) {
		int recoveredCases = 0;
		try {
			WebElement stateRow = getStateRowElement(state);
			String parsedElement = ".//td[%s]//span[@class='total']";
			recoveredCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("Recovered")))).getText().replace(",", "").replace("-", "0"));
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
		return recoveredCases;
	}
	
	
	public void validateAllStatesCaseCalculation() {
		Iterator<WebElement> iterator = stateRows.iterator();
		while (iterator.hasNext()) {
			validateStateUICalculation(iterator.next().findElement(By.xpath(String.format(".//td[%s]", getColumnNumber("State/UT")))).getText());
		}
	}
	
	public List<String> getAllStateList() {
		List<String> states = new ArrayList<>();
		Iterator<WebElement> iterator = stateRows.iterator();
		while (iterator.hasNext()) {
			states.add(iterator.next().findElement(By.xpath(String.format(".//td[%s]", getColumnNumber("State/UT")))).getText());
		}
		return states;
	}
	
	public void viewPatientDataBasePage() {
		try {
		executor.executeScript("arguments[0].scrollIntoView(true);", patientDatabaseProviderButton);
		LOGGER.info("Clicking on " + patientDatabaseProviderButton.getText() + " button");
		patientDatabaseProviderButton.click();
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
	}
	
	public Map<String, List<String>> getDistrictsAsPerZoneForAllStates(String zone) {
		Iterator<String> stateIterator = getAllStateList().iterator();
		Map<String, List<String>> resultantMap = new HashMap<>();
		while (stateIterator.hasNext()) {
			String state = stateIterator.next();
			resultantMap.put(state, getDistrictsAsPerZone(state, zone));
		}
		return resultantMap;
	}
	
	public List<String> getDistrictsAsPerZone(String state, String zone) {
		WebElement stateRow = getStateRowElement(state);
		String zoneModified = zone.isEmpty() || zone.equals("undefined") ? zone : zone.replaceFirst(String.valueOf(zone.charAt(0)), String.valueOf(zone.charAt(0)).toUpperCase());
		List<String> districtList = new ArrayList<>();
		WebElement clickableStateDropdown = webElementModifier.appendWebElement(stateRow, By.xpath(".//span[@class='dropdown rotateDownRight']"));
		this.executor.executeScript("arguments[0].click();", clickableStateDropdown);
		List<WebElement> districts = webElementModifier.appendWebElementToList(stateRow, By.xpath("//following-sibling::tr[@class='district']"));
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(districts));
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_" + state + "_" + "Districts_Show");
		Iterator<WebElement> districtListIterator = districts.stream().filter(x-> webElementModifier.appendWebElement(x, By.xpath(".//td")).getAttribute("class").equals("is-"+zoneModified)).collect(Collectors.toList()).iterator();
		while (districtListIterator.hasNext()) {
			districtList.add(webElementModifier.appendWebElement(districtListIterator.next(), By.xpath(".//div[@class='title-chevron']")).getText().trim());
		}
		webElementModifier.appendWebElement(stateRow, By.xpath(".//span[@class='dropdown rotateRightDown']")).click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_" + state + "_" + "Districts_Hide");
		return districtList;
	}

}
