package com.ui.testautomation.pageobjectmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
	
	private WebDriver driver;
	private WebDriverWait explicitWait;
	private JavascriptExecutor executor;
	private static final Logger LOGGER = Logger.getLogger(Covid19IndiaDashboard.class);
	
	@Autowired
	private WebElementModifier webElementModifier;
	
	@FindBy(xpath = "//div[@class = 'level-item is-cherry fadeInUp']/h5/following-sibling::h1")
	private WebElement noOfConfirmedCases;
	
	@FindBy(css = "tr.state")
	private List<WebElement> stateRows;
	
	@FindBy(xpath = "//thead//th")
	private List<WebElement> stickyHeads;
	
	@FindBy(xpath = "//a[@class='button excel']")
	private WebElement patientDatabaseProviderButton;
	
	@Autowired 
	public Covid19IndiaDashboard(Driver webDriver) { 
		this.driver = webDriver.getDriver();
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
		LOGGER.info("Column number with header " + columnName + " : " + columnNumber);
		return columnNumber;
	}
	
	public WebElement getStateRowElement(String state) {
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stateRows));
		List<WebElement> requiredWebElement = stateRows.stream().filter(x-> webElementModifier.appendWebElement(x, By.xpath(".//div[@class='table__title-wrapper']")).getText().trim().equalsIgnoreCase(state)).collect(Collectors.toList());
		return requiredWebElement.get(0);
	}
	
	public void browseToDashboard() {
		String url = "http://www.covid19India.org";
		getDriver().navigate().to(url);
		LOGGER.info("Navigated to : " + url);
	}
	
	public int getTotalConfirmedCasesCount() {
		int totalNumberOfConfirmedCases = Integer.parseInt(noOfConfirmedCases.getText().replace(",", "").trim());
		LOGGER.info("Total number of confirmed COVID19 cases in India : " + totalNumberOfConfirmedCases);
		return totalNumberOfConfirmedCases;
	}
	
	public void validateStateCalculation(String state) {
		WebElement stateRow = getStateRowElement(state);
		String parsedElement = ".//td[%s]";
		String spanTag = "//span[@class='table__count-text']";
		int confirmedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("CONFIRMED")) + spanTag)).getText().replace(",", ""));
		int activeCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("ACTIVE")))).getText().replace(",", "").replace("-", "0"));
		int recoveredCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("RECOVERED")) + spanTag)).getText().replace(",", "").replace("-", "0"));
		int deceasedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("DECEASED")) + spanTag)).getText().replace(",", "").replace("-", "0"));
		LOGGER.info("State :" + state);
		LOGGER.info("Confirmed : " + confirmedCases + ", Active : " + activeCases +", Recovered : " + recoveredCases + ", Deceased : " + deceasedCases);
		LOGGER.info("Verified Active Cases : Confirmed - Recovered + deceased");
		LOGGER.info(confirmedCases + " - " + recoveredCases + " + " + deceasedCases + " = " + (confirmedCases - recoveredCases - deceasedCases));
		Assert.assertTrue(activeCases == (confirmedCases - recoveredCases - deceasedCases), "Case calculations are not matching");
	}
	
	
	public void validateAllStatesCaseCalculation() {
		Iterator<WebElement> iterator = stateRows.iterator();
		while (iterator.hasNext()) {
			validateStateCalculation(iterator.next().findElement(By.xpath(String.format(".//td[%s]", getColumnNumber("STATE/UT")))).getText());
		}
	}
	
	public void viewPatientDataBasePage() {
		executor.executeScript("arguments[0].scrollIntoView(true);", patientDatabaseProviderButton);
		LOGGER.info("Clicking on " + patientDatabaseProviderButton.getText() + " button");
		patientDatabaseProviderButton.click();
	}

}
