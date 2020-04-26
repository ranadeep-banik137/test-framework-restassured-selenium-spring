package com.ui.testautomation;

import java.lang.reflect.Method;


import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.testautomation.configurations.Links;
import com.app.testautomation.initiators.BaseSteps;
import com.app.testautomation.initiators.RestCall;
import com.app.testautomation.listeners.BasicTestListeners;
import com.app.testautomation.listeners.MethodInterceptorListener;

@Listeners({BasicTestListeners.class, MethodInterceptorListener.class})
public class CovidIndiaUITest extends BaseSteps {
	
	private static final Logger LOGGER = Logger.getLogger(CovidIndiaUITest.class);
	
	@BeforeMethod
	public void initiateMethod(Method method) {
		initiateDriver();
		LOGGER.info("Initiating Method : " + method.getName());
		getCovid19Dashboard().browseToDashboard();
	}
	
	@Test(enabled = true, description = "validate the number of total confirmed case is greater than 6000 in India")
	public void checkNoOfConfirmedCasesIsGreaterThan6000() {
		int numberOfCases = getCovid19Dashboard().getTotalConfirmedCasesCount();
		Assert.assertTrue(numberOfCases >= 6000, "Number of cases not greater than 6k");
	}
	
	@Test(enabled = true, groups = {"sanity"}, description = "Checks & verify the total case calculation after recovered and death for Maharashtra")
	public void checkCaseCalculationsForDelhi() {
		getCovid19Dashboard().validateStateCalculation("DELHI");
		String apiCallResponse = getRestCall().link(Links.STATE_DISTRICT).getResponse();
		System.out.println(apiCallResponse);
	}
	
	@Test(groups = {"smoke"}, description = "Checks & verify the total case calculation after recovered and death for Chandigarh")
	public void checkCaseCalculationsForChandigarh() {
		getCovid19Dashboard().validateStateCalculation("CHANDIGARH");
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
