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
	
	@FindBy(xpath = "//tr[@class='state']")
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
	 
	
	public int getColumnNumber(String ColumnName) {
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stickyHeads));
		List<String> columnNameList = new ArrayList<>();
		stickyHeads.forEach(x-> {
			columnNameList.add(x.getText().trim());
		});
		return columnNameList.indexOf(ColumnName) + 1;
	}
	
	public WebElement getStateRowElement(String state) {
		this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stateRows));
		return stateRows.stream().filter(x-> webElementModifier.appendWebElement(x, By.cssSelector("div.table__title-wrapper")).getText().equals(state)).collect(Collectors.toList()).get(0);
	}
	
	public void BrowseToDashboard() {
		getDriver().navigate().to("http://www.covid19India.org");
	}
	
	public int getTotalConfirmedCasesCount() {
		return Integer.parseInt(noOfConfirmedCases.getText().trim());
	}
	
	public void validateStateCalculation(String state) {
		WebElement stateRow = getStateRowElement(state);
		String parsedElement = "//td[%s]";
		int confirmedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("CONFIRMED")) + "//span[@class='table__count-text']")).getText());
		int activeCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("ACTIVE")))).getText());
		int recoveredCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("RECOVERED")) + "//span[@class='table__count-text']")).getText());
		int deceasedCases = Integer.parseInt(webElementModifier.appendWebElement(stateRow, By.xpath(String.format(parsedElement, getColumnNumber("DECEASED")) + "//span[@class='table__count-text']")).getText());
		System.out.println("State :" + state);
		System.out.println("Confirmed : " + confirmedCases + ", Active : " + activeCases +", Recovered : " + recoveredCases + ", Deceased : " + deceasedCases);
		System.out.println("Verified Active Cases : Confirmed - Recovered + deceased");
		System.out.println(confirmedCases + " - " + recoveredCases + " + " + deceasedCases + " = " + (confirmedCases - recoveredCases - deceasedCases));
		Assert.assertTrue(activeCases == (confirmedCases - recoveredCases - deceasedCases), "Case calculations are not matching");
	}
	
	
	public void validateAllStatesCaseCalculation() {
		Iterator<WebElement> iterator = stateRows.iterator();
		while (iterator.hasNext()) {
			validateStateCalculation(iterator.next().findElement(By.xpath(String.format("//td[%s]", getColumnNumber("STATE/UT")))).getText());
		}
	}
	
	public void viewPatientDataBasePage() {
		executor.executeScript("arguments[0].scrollIntoView(true);", patientDatabaseProviderButton);
		patientDatabaseProviderButton.click();
	}

}
