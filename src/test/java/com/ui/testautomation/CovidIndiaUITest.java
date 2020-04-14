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

import com.app.testautomation.initiators.BaseSteps;
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
	}
	
	@Test(groups = {"smoke"}, description = "Checks & verify the total case calculation after recovered and death for Chandigarh")
	public void checkCaseCalculationsForChandigarh() {
		getCovid19Dashboard().validateStateCalculation("CHANDIGARH");
	}
	
	@Test(enabled = true, groups = {"smoke", "regression"}, description = "Checks & verify the total case calculation after recovered and death for all 32 states of INDIA")
	public void checkCaseCalculationsForAllStates() {
		getCovid19Dashboard().validateAllStatesCaseCalculation();
	}
	
	@AfterMethod(enabled = true)
	public void finish(Method method) {
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
