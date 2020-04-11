package com.ui.testautomation;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.app.testautomation.initiators.BaseSteps;

public class CovidIndiaUITest extends BaseSteps {
	
	@BeforeMethod
	public void initiateMethod(Method method) {
		//TODO
	}
	
	@Test
	public void checkNoOfConfirmedCasesIsGreaterThan6000() {
		Assert.assertTrue(covid19Dashboard.getTotalConfirmedCasesCount() > 6000);
	}
}
