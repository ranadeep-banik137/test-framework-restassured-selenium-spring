package com.app.testautomation.utilities;

import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.app.testautomation.initiators.Driver;

@Component(value = "assertions")
public class Assertions {
	
	private Driver webDriver;

	public Assertions(Driver webDriver) {
		this.webDriver = webDriver;
	}
	
	public void assertTrue(boolean condition) {
		catchScreenIfFailed(condition);
		Assert.assertTrue(condition);
	}

	public void assertTrue(boolean condition, String message) {
		catchScreenIfFailed(condition);
		Assert.assertTrue(condition, message);
	}
	
	private void catchScreenIfFailed(boolean condition) {
		if (!condition) {
			this.webDriver.takeScreenShot("explicit_failed");
		}
	}
}
