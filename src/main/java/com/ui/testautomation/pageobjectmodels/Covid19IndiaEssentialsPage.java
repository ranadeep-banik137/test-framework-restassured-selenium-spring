package com.ui.testautomation.pageobjectmodels;

import static com.app.testautomation.initiators.SystemVariables.*;
import static com.app.testautomation.utilities.Perform.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.app.testautomation.initiators.Driver;
import com.app.testautomation.pojos.City;
import com.app.testautomation.pojos.Locations;
import com.app.testautomation.pojos.State;
import com.app.testautomation.utilities.WebElementModifier;

@Component(value = "covid19Essentials")
public class Covid19IndiaEssentialsPage {

	private static final Logger LOGGER = Logger.getLogger(Covid19IndiaEssentialsPage.class);
	private static final String PAGE_SOURCE = "Covid19IndiaEssentials";
	
	private Driver webDriverInitiator;
	private WebDriver driver;
	private WebDriverWait explicitWait;
	private JavascriptExecutor executor;
	private Actions actions;
	@Autowired
	private WebElementModifier webElementModifier;
	@Autowired
	private Locations locations;
	
	
	@FindBy(xpath = "//div[@class='navbar-right']//a[@href='/']")
	private WebElement navigateToHomeButton;
	
	@FindBy(xpath = "//div[@class='expand']//a[@href='/essentials']")
	private WebElement navigateToExpandedEssentialsTab;
	
	@FindBy(xpath = "//div[@class= 'button desktop-disclaimer-button']")
	private WebElement disclaimerTab;
	
	@FindBy(xpath = "//div[@class= 'MuiPaper-root MuiPopover-paper MuiPaper-elevation8 MuiPaper-rounded']")
	private WebElement disclaimerInfo;
	
	@FindBy(xpath = "//select[@id= 'stateselect']")
	private WebElement selectStateDropdown;
	
	@FindBy(xpath = "//select[@id= 'cityselect1']")
	private WebElement selectCityDropdown;
	
	@FindBy(xpath = "//select[@id= 'categoryselect']")
	private WebElement selectCategoryDropdown;
	
	@FindBy(xpath = "//button[contains(@class,'search-button')]")
	private WebElement searchButton;
	
	@FindBy(xpath = "//table[@role='table']")
	private WebElement searchResultTable;
	
	@Autowired 
	public Covid19IndiaEssentialsPage(Driver webDriver) { 
		this.webDriverInitiator = webDriver;
		this.driver = webDriver.getDriver();
		this.actions = new Actions(this.driver);
		PageFactory.initElements(getDriver(), this);
		this.explicitWait = new WebDriverWait(getDriver(), 15);
		this.executor = (JavascriptExecutor) getDriver();
	}
	
	public WebDriver getDriver() { return driver; }
	
	public void browseToEssentials() {
		try {
		this.explicitWait.until(ExpectedConditions.elementToBeClickable(navigateToHomeButton));
		this.actions.moveToElement(navigateToHomeButton).perform();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		this.explicitWait.until(ExpectedConditions.visibilityOf(navigateToExpandedEssentialsTab));
		this.navigateToExpandedEssentialsTab.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		this.explicitWait.until(ExpectedConditions.visibilityOf(disclaimerTab));
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		} catch (Exception exception) {
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
	}
	
	public void setUIData() {
		setValue(SCREEN_CAPTURE_FLAG, "false");
		List<State> states = new ArrayList<>();
		//selectStateDropdown.click();
		List<WebElement> stateElements = this.webElementModifier.appendWebElementToList(selectStateDropdown, By.xpath(".//option"));
		//this.explicitWait.until(ExpectedConditions.visibilityOfAllElements(stateElements));
		stateElements.forEach(state -> {
			State stateInstance = new State();
			stateInstance.setStateName(selectState(state.getText()));
			List<City> cities = new ArrayList<>();
			List<WebElement> cityElements = this.webElementModifier.appendWebElementToList(selectCityDropdown, By.xpath(".//option"));
			cityElements.forEach(city -> {
				City cityInstance = new City();
				cityInstance.setCityName(selectCity(city.getText()));
				cities.add(cityInstance);
				List<String> categories = new ArrayList<>();
				List<WebElement> categoryElements = this.webElementModifier.appendWebElementToList(selectCategoryDropdown, By.xpath(".//option"));
				categoryElements.forEach(category -> {
					categories.add(selectCategory(category.getText()));
				});
				cityInstance.setCategories(categories);
			});
			states.add(stateInstance);
		});
		locations.setStates(states);
		setValue(SCREEN_CAPTURE_FLAG, "true");
	}
	
	public void setAllRandomEssentialData() {
		final String RANDOM = "random";
		State state = new State();
		City city = new City();
		state.setStateName(selectState(RANDOM));
		city.setCityName(selectCity(RANDOM));
		city.addCategory(selectCategory(RANDOM));
		state.addCity(city);
		locations.addState(state);
	}
	
	public String selectState(String state) {
		List<WebElement> states = getAllStateList();
		if (state.equalsIgnoreCase("random")) {
			WebElement randomStateElement = states.get(getRandomNumber(1, states.size()-1));
			state = state.contains("PAN") ? "PAN India" : formatFirstLetterInCaps(randomStateElement.getText().trim().toLowerCase());
			randomStateElement.click();
		} else {
			Iterator<WebElement> stateIterator = states.iterator();
			while (stateIterator.hasNext()) {
				WebElement stateElement = stateIterator.next();
				this.executor.executeScript("arguments[0].scrollIntoView(true);", stateElement);
				this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
				if (stateElement.getText().equalsIgnoreCase(state)) {
					stateElement.click();
					break;
				}
			}
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		}
		return state;
	}
	
	public String selectCity(String city) {
		List<WebElement> cities = getAllCityList();
		if (city.equalsIgnoreCase("random")) {
			WebElement randomCityElement = cities.get(getRandomNumber(1, cities.size()-1));
			city = city.contains("PAN") ? "PAN State" :formatFirstLetterInCaps(randomCityElement.getText().trim().toLowerCase());
			randomCityElement.click();
		} else {
			Iterator<WebElement> cityIterator = cities.iterator();
			while (cityIterator.hasNext()) {
				WebElement cityElement = cityIterator.next();
				this.executor.executeScript("arguments[0].scrollIntoView(true);", cityElement);
				this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
				if (cityElement.getText().equalsIgnoreCase(city)) {
					cityElement.click();
					break;
				}
			}
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		}
		return city;
	}
	
	public String selectCategory(String category) {
		List<WebElement> categories = getAllCategoryList();
		if (category.equalsIgnoreCase("random")) {
			WebElement randomCategoryElement = categories.get(getRandomNumber(1, categories.size()-1));
			category = formatFirstLetterInCaps(randomCategoryElement.getText().trim().toLowerCase());
			randomCategoryElement.click();
		} else {
			Iterator<WebElement> categoryIterator = categories.iterator();
			while (categoryIterator.hasNext()) {
				WebElement categoryElement = categoryIterator.next();
				this.executor.executeScript("arguments[0].scrollIntoView(true);", categoryElement);
				this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
				if (categoryElement.getText().equalsIgnoreCase(category)) {
					categoryElement.click();
					break;
				}
			}
			this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		}
		return category;
	}
	
	public void clickSearch() {
		this.explicitWait.until(ExpectedConditions.elementToBeClickable(searchButton));
		searchButton.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		this.explicitWait.until(ExpectedConditions.visibilityOf(searchResultTable));
	}
	
	public List<Map<String, String>> getTabularMappedData() {
		Iterator<WebElement> tableRowIterator = this.webElementModifier.appendWebElementToList(searchResultTable, By.xpath(".//tbody/tr")).iterator();
		List<Map<String, String>> tablularList = new ArrayList<>();
		while (tableRowIterator.hasNext()) {
			WebElement row = tableRowIterator.next();
			this.executor.executeScript("arguments[0].scrollIntoView(true);", row);
			Iterator<WebElement> rowValues = this.webElementModifier.appendWebElementToList(row, By.xpath(".//td")).iterator();
			Iterator<WebElement> tableHeaderColumnElementIterator = this.webElementModifier.appendWebElementToList(searchResultTable, By.xpath(".//thead/tr/th")).iterator();
			Map<String, String> tableMapper = new HashMap<>();
			while (tableHeaderColumnElementIterator.hasNext() && rowValues.hasNext()) {
				String columnName = tableHeaderColumnElementIterator.next().getText().trim();
				tableMapper.put(columnName, rowValues.next().getText().trim());
			}
			tablularList.add(tableMapper);
		}
		return tablularList;
	}
	
	public int getColumnNumber(String column) {
		Iterator<WebElement> tableHeaderColumnElementIterator = this.webElementModifier.appendWebElementToList(searchResultTable, By.xpath(".//thead/tr/th")).iterator();
		int columnCount = 0;
		while (tableHeaderColumnElementIterator.hasNext()) {
			if (tableHeaderColumnElementIterator.next().getText().equals(column)) {
				break;
			}
			columnCount += 1;
		}
		return columnCount;
	}
	
	public List<WebElement> getAllStateList() {
		selectStateDropdown.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		return this.webElementModifier.appendWebElementToList(selectStateDropdown, By.xpath(".//option"));
	}
	
	public List<WebElement> getAllCityList() {
		selectCityDropdown.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		return this.webElementModifier.appendWebElementToList(selectCityDropdown, By.xpath(".//option"));
	}
	
	public List<WebElement> getAllCategoryList() {
		selectCategoryDropdown.click();
		this.webDriverInitiator.takeScreenShot(PAGE_SOURCE);
		return this.webElementModifier.appendWebElementToList(selectCategoryDropdown, By.xpath(".//option"));
	}
}
