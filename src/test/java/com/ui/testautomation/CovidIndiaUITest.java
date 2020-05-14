package com.ui.testautomation;

import static com.app.testautomation.utilities.Perform.*;

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
import com.app.testautomation.exceptions.MultipleValuesReturnedException;
import com.app.testautomation.listeners.BasicTestListeners;
import com.app.testautomation.listeners.MethodInterceptorListener;
import com.app.testautomation.pojos.City;
import com.app.testautomation.pojos.State;
import com.ui.testautomation.pageobjectmodels.PageInitiator;

import io.restassured.path.json.JsonPath;

@Listeners({BasicTestListeners.class, MethodInterceptorListener.class})
public class CovidIndiaUITest extends PageInitiator {
	
	private static final Logger LOGGER = Logger.getLogger(CovidIndiaUITest.class);
	
	@BeforeMethod
	public void initiateMethod(Method method) {
		initiateDriver();
		LOGGER.info("Initiating Method : " + method.getName());
		browseToCovid19IndiaSite();
	}
	
	@Test(groups = {"dashboard"}, enabled = true, description = "validate the number of total confirmed case is greater than 6000 in India")
	public void checkNoOfConfirmedCasesIsGreaterThan26000() {
		getCovid19Dashboard().browseToDashboard();
		int numberOfCases = getCovid19Dashboard().getTotalConfirmedCasesCount();
		Assert.assertTrue(numberOfCases >= 26000, "Number of cases not greater than 6k");
	}
	
	@Test(enabled = true, groups = {"sanity", "dashboard"}, description = "Checks & verify the total case calculation after recovered and death for Maharashtra")
	public void checkCaseCalculationsForDelhi() {
		String state = "DELHI";
		getCovid19Dashboard().browseToDashboard();
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
	
	@Test(groups = {"smoke", "dashboard"}, description = "Checks & verify the total case calculation after recovered and death for Chandigarh")
	public void checkCaseCalculationsForChandigarh() {
		String state = "CHANDIGARH";
		getCovid19Dashboard().browseToDashboard();
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
	
	@Test(enabled = true, groups = {"smoke", "regression", "dashboard"}, description = "Checks & verify the total case calculation after recovered and death for all 32 states of INDIA")
	public void checkCaseCalculationsForAllStates() {
		getCovid19Dashboard().browseToDashboard();
		getCovid19Dashboard().validateAllStatesCaseCalculation();
	}
	
	@Test(enabled = true, groups = {"smoke", "dashboard"}, description = "check case count of Tripura is more than 10")
	public void checkCaseCalculationsOfTripuraIsGreaterThan10() {
		getCovid19Dashboard().browseToDashboard();
		int cases = getCovid19Dashboard().getTotalConfirmedCountOf("TRIPURA");
		Assert().assertTrue(cases > 10, "Case count is not greater than 10");
	}
	
	/** Zone testing */
	
	@Test(groups = {"sanity", "smoke", "regression", "zones"}, description = "Check the Red zones of all states")
	public void verifyRedZonesOfAllStates() {
		getCovid19Dashboard().browseToDashboard();
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("red");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Red'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Red Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression", "zones"}, description = "Check the Green zones of all states")
	public void verifyGreenZonesOfAllStates() {
		getCovid19Dashboard().browseToDashboard();
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("green");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Green'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Green Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression", "zones"}, description = "Check the Orange zones of all states")
	public void verifyOrangeZonesOfAllStates() {
		getCovid19Dashboard().browseToDashboard();
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("orange");
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == 'Orange'}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api Orange Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"regression", "zones"}, description = "Check the White unknown zones of all states")
	public void verifyUnknownWhiteZonesOfAllStates() {
		getCovid19Dashboard().browseToDashboard();
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates(StringUtils.EMPTY);
		String apiCallResponse = restCall().link(Links.ZONES).getResponse();
		zoneMapper.forEach((state,list) -> {
			List<String> apiList = JsonPath.from(apiCallResponse).getList("zones.findAll { it.state == '" + state + "' && it.zone == ''}.district");
			Assert().assertTrue(apiList.containsAll(list), "Api White Listed District List Is Not Matching With UI List");
		});
	}
	
	@Test(groups = {"smoke", "zones"}, description = "Check the unknown undefined zones of all states")
	public void verifyUnDefinedZonesOfAllStates() {
		getCovid19Dashboard().browseToDashboard();
		Map<String, List<String>> zoneMapper = getCovid19Dashboard().getDistrictsAsPerZoneForAllStates("undefined");
		zoneMapper.forEach((state,list) -> {
			System.out.println(state);
			System.out.println(list);
			Assert().assertTrue(list.isEmpty() ? true : list.contains("Unknown") || list.contains("Other State") || list.contains("Other Region") || list.contains("Airport Quarantine") || list.contains("Italians"), "Api White Listed District List Is Not Matching With UI List");
		});
	}
	
	
	/** Essentials testing */
	
	@Test(groups = {"sanity", "essentials"}, description = "Verify the CoVID-19 Testing Labs Listed for Pune, Maharashtra with API call")
	public void verifyCovidTestingLabOfPune() {
		getCovid19Essentials().browseToEssentials();
		getCovid19Essentials().selectState("Maharashtra");
		getCovid19Essentials().selectCity("Pune");
		getCovid19Essentials().selectCategory("CoVID-19 Testing Lab");
		getCovid19Essentials().clickSearch();
		List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		tableData.forEach(element-> {
			JsonPath root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll {it.category == 'CoVID-19 Testing Lab' && it.city == 'Pune' && it.state == 'Maharashtra' && it.nameoftheorganisation == '" + element.get("Organisation") + "' }");
			String description = root.getString("descriptionandorserviceprovided").replaceAll("\\[|\\]", "").trim();
			String phoneNumber = root.getString("phonenumber").replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", "");
			Assert().assertTrue(element.get("Description").equals(description) && element.get("Phone").equals(phoneNumber), "Api doesnot match with UI");
		});
	}
	
	@Test(groups = {"sanity", "regression", "essentials"}, description = "Verify the CoVID-19 Testing Labs Listed for Imphal, Manipur with API call")
	public void verifyCovidTestingLabOfImphal() {
		getCovid19Essentials().browseToEssentials();
		getCovid19Essentials().selectState("Manipur");
		getCovid19Essentials().selectCity("Imphal");
		getCovid19Essentials().selectCategory("CoVID-19 Testing Lab");
		getCovid19Essentials().clickSearch();
		List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		tableData.forEach(element-> {
			JsonPath root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll {it.category == 'CoVID-19 Testing Lab' && it.city == 'Imphal' && it.state == 'Manipur' && it.nameoftheorganisation == '" + element.get("Organisation") + "' }");
			String description = root.getString("descriptionandorserviceprovided").replaceAll("\\[|\\]", "").trim();
			String phoneNumber = root.getString("phonenumber").replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", "");
			Assert().assertTrue(element.get("Description").equals(description) && element.get("Phone").equals(phoneNumber), "Api doesnot match with UI");
		});
	}
	
	@Test(groups = {"regression", "essentials"}, description = "Verify random essentials listed with API call")
	public void verifyEssentialsRandomly() {
		getCovid19Essentials().browseToEssentials();
		getCovid19Essentials().setAllRandomEssentialData();
		getCovid19Essentials().clickSearch();
		List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		tableData.forEach(element-> {
			JsonPath root;
			try {
				Assert.assertTrue(getLocations().getState().getCity().getCityName().trim().equals(element.get("City")));
				Assert.assertTrue(getLocations().getState().getCity().getCategory().trim().equalsIgnoreCase(element.get("Category")));
				root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll {it.category == '" + element.get("Category") +"' && it.city == '" + element.get("City") +"' && it.state == '" + getLocations().getState().getStateName().trim() + "' }");
				List<String> description = root.getList("descriptionandorserviceprovided");
				description.forEach(x-> { x.replaceAll("\\[|\\]", "").trim(); });
				List<String> phoneNumber = root.getList("phonenumber");
				phoneNumber.forEach(x-> { x.replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", ""); });
				List<String> organization = root.getList("nameoftheorganisation");
				organization.forEach(x-> { x.replaceAll("\\[|\\]", "").trim(); });
				Assert().assertTrue(description.contains(element.get("Description")) && phoneNumber.contains(element.get("Phone")) && organization.contains(element.get("Organisation")), "Api doesnot match with UI");
			} catch (MultipleValuesReturnedException exception) {
				exception.printStackTrace();
			}
		});
	}
	
	@Test(groups = {"sanity", "smoke", "essentials"}, description = "Verify all the categories Listed for Raipur with API call")
	public void verifyAllCategoriesOfRaipurChhattisgarh() {
		getCovid19Essentials().browseToEssentials();
		getCovid19Essentials().selectState("Chhattisgarh");
		getCovid19Essentials().selectCity("Raipur");
		getCovid19Essentials().selectCategory("All categories");
		getCovid19Essentials().clickSearch();
		List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		tableData.forEach(element-> {
			JsonPath root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll { it.category == '"+ element.get("Category") +"' && it.city == 'Raipur' && it.state == 'Chhattisgarh' && it.nameoftheorganisation == '" + element.get("Organisation") + "' }");
			String description = root.getString("descriptionandorserviceprovided").replaceAll("\\[|\\]", "").trim();
			String phoneNumber = root.getString("phonenumber").replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", "");
			Assert().assertTrue(element.get("Description").equals(description) && element.get("Phone").equals(phoneNumber), "Api doesnot match with UI");
		});
	}
	
	@Test(groups = {"regression", "essentials"}, description = "Verify Delivery categories Listed for faridkot with API call")
	public void verifyDeliveryCategoriesOfFaridkotPunjab() {
		getCovid19Essentials().browseToEssentials();
		getCovid19Essentials().selectState("Punjab");
		getCovid19Essentials().selectCity("Faridkot");
		getCovid19Essentials().selectCategory("Delivery [vegetables, Fruits, Groceries, Medicines, Etc.]");
		getCovid19Essentials().clickSearch();
		List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		tableData.forEach(element-> {
			JsonPath root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll { it.state == 'Punjab' && it.city == '" + element.get("City") + "' && it.category == '" + element.get("Category") + "' && it.nameoftheorganisation == '" + element.get("Organisation") + "' }");
			String description = root.getString("descriptionandorserviceprovided").replaceAll("\\[|\\]", "").trim();
			String phoneNumber = root.getString("phonenumber").replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", "");
			Assert().assertTrue(element.get("Description").equals(description) && element.get("Phone").equals(phoneNumber.replaceFirst(",", "")), "Api doesnot match with UI");
		});
	}
	
	@Test(groups = {"regression", "essentials"}, description = "Verify all essentials for a random state with API call")
	public void verifyAllEssentialsForRandomState() {
		final String RANDOM = "random";
		String apiCallResponse = restCall().link(Links.ESSENTIALS).getResponse();
		getCovid19Essentials().browseToEssentials();
		getLocations().addState(getCovid19Essentials().selectState(RANDOM));
		getCovid19Essentials().getAllCityList().forEach(city-> {
			try {
				City cityInstance = new City();
				State stateInstance = getLocations().getState();
				cityInstance.setCityName(getCovid19Essentials().selectCity(city.getText()));
				cityInstance.addCategory(getCovid19Essentials().selectCategory(RANDOM));
				stateInstance.addCity(cityInstance);
				getCovid19Essentials().clickSearch();
				List<Map<String, String>> tableData = getCovid19Essentials().getTabularMappedData();
				tableData.forEach(element-> {
					JsonPath root = JsonPath.from(apiCallResponse).setRootPath("resources.findAll { it.category == '" + element.get("Category") +"' && it.city == '" + element.get("City") +"' && it.state == '" + stateInstance.getStateName().trim() + "' }");
					List<String> organization = root.getList("nameoftheorganisation");
					organization.forEach(x-> {x.replaceAll("\\[|\\]", "").trim();});
					List<String> description = root.getList("descriptionandorserviceprovided");
					description.forEach(x-> {x.replaceAll("\\[|\\]", "").trim();});
					List<String> phoneNumber = root.getList("phonenumber");
					phoneNumber.forEach(x-> {x.replaceAll("\\[|\\]", StringUtils.EMPTY).trim().replaceAll(",", ""); });
					Assert().assertTrue(organization.contains(element.get("Organisation")) && description.contains(element.get("Description")) && phoneNumber.contains(element.get("Phone")), "Api doesnot match with UI");
				});
			} catch (MultipleValuesReturnedException exception) {
				exception.printStackTrace();
			}
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
