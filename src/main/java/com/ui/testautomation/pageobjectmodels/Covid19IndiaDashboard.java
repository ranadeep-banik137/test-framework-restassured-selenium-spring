package com.ui.testautomation.pageobjectmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
	WebDriverWait explicitWait;
	JavascriptExecutor executor;
	
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
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stickyHeads));
		List<String> columnNameList = new ArrayList<>();
		Iterator<WebElement> iterator = stickyHeads.listIterator();
		while (iterator.hasNext()) {
			columnNameList.add(iterator.next().getText().trim());
		}
		return columnNameList.indexOf(columnName) + 1;
	}
	
	public WebElement getStateRowElement(String state) {
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stateRows));
		List<WebElement> requiredWebElement = stateRows.stream().filter(x-> webElementModifier.appendWebElement(x, By.xpath(".//div[@class='table__title-wrapper']")).getText().trim().equalsIgnoreCase(state)).collect(Collectors.toList());
		return requiredWebElement.get(0);
	}
	
	public void browseToDashboard() {
		getDriver().navigate().to("http://www.covid19India.org");
	}
	
	public int getTotalConfirmedCasesCount() {
		return Integer.parseInt(noOfConfirmedCases.getText().replace(",", "").trim());
	}
	
	public void validateStateCalculation(String state) {
		WebElement stateRow = getStateRowElement(state);
		String parsedElement = ".//td[%s]";
		String spanTag = "//span[@class='table__count-text']";
		int confirmedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("CONFIRMED")) + spanTag)).getText().replace(",", ""));
		int activeCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("ACTIVE")))).getText().replace(",", "").replace("-", "0"));
		int recoveredCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("RECOVERED")) + spanTag)).getText().replace(",", "").replace("-", "0"));
		int deceasedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("DECEASED")) + spanTag)).getText().replace(",", "").replace("-", "0"));
		System.out.println("State :" + state);
		System.out.println("Confirmed : " + confirmedCases + ", Active : " + activeCases +", Recovered : " + recoveredCases + ", Deceased : " + deceasedCases);
		System.out.println("Verified Active Cases : Confirmed - Recovered + deceased");
		System.out.println(confirmedCases + " - " + recoveredCases + " + " + deceasedCases + " = " + (confirmedCases - recoveredCases - deceasedCases));
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
		patientDatabaseProviderButton.click();
	}

}
