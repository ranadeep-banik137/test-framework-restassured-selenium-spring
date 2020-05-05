package com.ui.testautomation;

import static com.app.testautomation.utilities.Perform.getCount;
import static com.app.testautomation.utilities.Perform.sumOf;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.testautomation.configurations.Links;
import com.app.testautomation.listeners.BasicTestListeners;
import com.app.testautomation.listeners.MethodInterceptorListener;
import com.ui.testautomation.pageobjectmodels.PageInitiator;

import io.restassured.path.json.JsonPath;

@Listeners({BasicTestListeners.class, MethodInterceptorListener.class})
public class CovidIndiaUITest extends PageInitiator {
	
	private static final Logger LOGGER = Logger.getLogger(CovidIndiaUITest.class);
	
	@BeforeMethod
	public void initiateMethod(Method method) {
		initiateDriver();
		LOGGER.info("Initiating Method : " + method.getName());
		getCovid19Dashboard().browseToDashboard();
	}
	
	@Test(enabled = true, description = "validate the number of total confirmed case is greater than 6000 in India")
	public void checkNoOfConfirmedCasesIsGreaterThan26000() {
		int numberOfCases = getCovid19Dashboard().getTotalConfirmedCasesCount();
		Assert.assertTrue(numberOfCases >= 26000, "Number of cases not greater than 6k");
	}
	
	@Test(enabled = true, groups = {"sanity"}, description = "Checks & verify the total case calculation after recovered and death for Maharashtra")
	public void checkCaseCalculationsForDelhi() {
		String state = "DELHI";
		getCovid19Dashboard().validateStateUICalculation(state);
		int confirmedCaseCount = getCovid19Dashboard().getTotalConfirmedCountOf(state);
		int activeCaseCount = getCovid19Dashboard().getTotalActiveCountOf(state);
		int recoveredCaseCount = getCovid19Dashboard().getTotalRecoveredCountOf(state);
		int deceasedCaseCount = getCovid19Dashboard().getTotalDeceasedCountOf(state);
		String apiCallResponse = restCall().link(Links.STATE_DISTRICT_V2).getResponse();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Delhi'}.districtData*.confirmed"));
		int apiConfirmedCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Delhi'}.districtData*.active"));
		int apiActiveCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Delhi'}.districtData*.recovered"));
		int apiRecoveredCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Delhi'}.districtData*.deceased"));
		int apiDeceasedCount = getCount();
		Assert().assertTrue((confirmedCaseCount == apiConfirmedCount) && (activeCaseCount == apiActiveCount) && (recoveredCaseCount == apiRecoveredCount) && (deceasedCaseCount == apiDeceasedCount), "Data on UI & API doesnot match");
	}
	
	@Test(groups = {"smoke"}, description = "Checks & verify the total case calculation after recovered and death for Chandigarh")
	public void checkCaseCalculationsForChandigarh() {
		String state = "CHANDIGARH";
		getCovid19Dashboard().validateStateUICalculation(state);
		int confirmedCaseCount = getCovid19Dashboard().getTotalConfirmedCountOf(state);
		int activeCaseCount = getCovid19Dashboard().getTotalActiveCountOf(state);
		int recoveredCaseCount = getCovid19Dashboard().getTotalRecoveredCountOf(state);
		int deceasedCaseCount = getCovid19Dashboard().getTotalDeceasedCountOf(state);
		String apiCallResponse = restCall().link(Links.STATE_DISTRICT_V2).getResponse();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Chandigarh'}.districtData*.confirmed"));
		int apiConfirmedCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Chandigarh'}.districtData*.active"));
		int apiActiveCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Chandigarh'}.districtData*.recovered"));
		int apiRecoveredCount = getCount();
		sumOf(JsonPath.from(apiCallResponse).getList("find { it.state == 'Chandigarh'}.districtData*.deceased"));
		int apiDeceasedCount = getCount();
		Assert().assertTrue((confirmedCaseCount == apiConfirmedCount) && (activeCaseCount == apiActiveCount) && (recoveredCaseCount == apiRecoveredCount) && (deceasedCaseCount == apiDeceasedCount), "Data on UI & API doesnot match");

	}
	
	@Test(enabled = true, groups = {"smoke", "regression"}, description = "Checks & verify the total case calculation after recovered and death for all 32 states of INDIA")
	public void checkCaseCalculationsForAllStates() {
		getCovid19Dashboard().validateAllStatesCaseCalculation();
	}
	
	@Test(enabled = true, groups = {"failed"}, description = "check case count of Tripura is more than 10")
	public void checkCaseCalculationsOfTripuraIsGreaterThan10() {
		int cases = getCovid19Dashboard().getTotalConfirmedCountOf("TRIPURA");
		Assert().assertTrue(cases > 10, "Case count is not greater than 10");
	}
	
	/** Zone testing */
	
	@Test(groups = {"sanity", "smoke", "regression"}, description = "Check the Red zones of all states")
	public void verifyRedZonesOfAllStates() {
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("red");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Red'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Red Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression"}, description = "Check the Green zones of all states")
	public void verifyGreenZonesOfAllStates() {
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("green");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Green'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Green Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression"}, description = "Check the Orange zones of all states")
	public void verifyOrangeZonesOfAllStates() {
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("orange");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Orange'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Orange Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression"}, description = "Check the White unknown zones of all states")
	public void verifyUnknownWhiteZonesOfAllStates() {
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates(StringUtils.EMPTY);
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == ''}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api White Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression"}, description = "Check the unknown undefined zones of all states")
	public void verifyUnDefinedZonesOfAllStates() {
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("undefined");
		zoneMapper.forEach((state,list) -> {
			Assert().assertTrue(list.isEmpty() ? true : list.contains("Unknown") || list.contains("Other State"), "Api White Listed District List Is Not Matching With UI List");
		});
	}
	
	@AfterMethod(enabled = true)
	public void finish(Method method) {
		getConverter().appendVideoFileName("_" + method.getName()).convert();
		LOGGER.info(method.getName() + " executed.");
		getDriver().close();
		LOGGER.info("Driver is closed");
		LOGGER.warn("Driver called off");
	}
	
	@AfterTest
	public void quit() {
		String windowHandle = null;
		try {
			windowHandle = getDriver().getWindowHandle();
		} catch(NoSuchSessionException session) {
			windowHandle = null;
		}
		if (windowHandle != null) {
			getDriver().quit();
			LOGGER.info("Driver quit");
			LOGGER.warn("Driver called off");
		}
	}
}
