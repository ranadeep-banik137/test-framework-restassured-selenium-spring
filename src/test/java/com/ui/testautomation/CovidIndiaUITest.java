package com.ui.testautomation;

import java.lang.reflect.Method;

import org.openqa.selenium.NoSuchSessionException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.app.testautomation.initiators.BaseSteps;

public class CovidIndiaUITest extends BaseSteps {
	
	@BeforeMethod
	public void initiateMethod(Method method) {
		//initiateDriver();
		getCovid19Dashboard().browseToDashboard();
	}
	
	@Test(enabled = false)
	public void checkNoOfConfirmedCasesIsGreaterThan6000() {
		int numberOfCases = getCovid19Dashboard().getTotalConfirmedCasesCount();
		if (numberOfCases >= 6000) {
			Assert.fail("Number of cases not greater than 6k");
		}
	}
	
	@Test(enabled = true)
	public void checkCaseCalculationsForMaharashtra() {
		getCovid19Dashboard().validateStateCalculation("DELHI");
	}
	
	@Test
	public void checkCaseCalculationsForChandigarh() {
		getCovid19Dashboard().validateStateCalculation("CHANDIGARH");
	}
	
	@Test
	public void checkCaseCalculationsForAllStates() {
		getCovid19Dashboard().validateAllStatesCaseCalculation();
	}
	
	@AfterMethod(enabled = false)
	public void finish() {
		getDriver().close();
	}
	
	@AfterTest
	public void quit() {
		String windowHandle = null;
		try {
			windowHandle = getDriver().getWindowHandle();
		} catch(NoSuchSessionException session) {
			windowHandle = null;
		}
		if (windowHandle != null)
			getDriver().quit();
	}
}
