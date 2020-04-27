package com.app.testautomation.listeners;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.util.logging.Logger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MethodRerunListener implements IRetryAnalyzer {

	private static final Logger LOGGER = Logger.getLogger(MethodRerunListener.class.getName());
	private static final int IMPLICIT_RETRY_COUNT = 2;
	private static int initialCount = 0;
	private boolean retryIfFailed = true;
	/**
	 * Idea by PRITAM MONDAL (static member initiation of initial count variable)
	 */
	@Override
	public boolean retry(ITestResult result) {
		retryIfFailed = getValue(RETRY_IF_FAILED) == null ? retryIfFailed : Boolean.getBoolean(RETRY_IF_FAILED);
		if (retryIfFailed) {
			int retry = (getValue(RETRY_COUNT) == null) ? IMPLICIT_RETRY_COUNT : Integer.parseInt(getValue(RETRY_COUNT));
			if (!result.isSuccess()) { 
				if (initialCount < retry) {
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
		} else {
			return retryIfFailed;
		}
	}

}
