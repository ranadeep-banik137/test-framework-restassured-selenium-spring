package com.app.testautomation.listeners;

import java.util.logging.Logger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MethodRerunListener implements IRetryAnalyzer {

	private static final Logger LOGGER = Logger.getLogger(MethodRerunListener.class.getName());
	private static final int RETRY_COUNT = 1;
	private static int initialCount = 0;
	
	/**
	 * Idea by PRITAM MONDAL (static member initiation of initial count variable)
	 */
	@Override
	public boolean retry(ITestResult result) {
		if (!result.isSuccess()) { 
			if (initialCount < RETRY_COUNT) {
            	initialCount++;
                result.setStatus(ITestResult.FAILURE);
                LOGGER.info("Re-executing the method : " + result.getName() + "With count : " + initialCount);
                return true;
            } else {
            	result.setStatus(ITestResult.FAILURE);
            }
        } else {
        	result.setStatus(ITestResult.SUCCESS);
        }
		return false;
	}

}
