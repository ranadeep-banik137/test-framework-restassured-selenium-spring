package com.app.testautomation.listeners;

import java.util.Date;
import java.util.logging.Logger;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class MethodInvokeListener implements IInvokedMethodListener {

	private static final Logger LOGGER = Logger.getLogger(MethodInvokeListener.class.getName());
	private Boolean isTestMethod;
	private Boolean isMethodEnabled;
	private String methodName;
	
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		setIsTestMethod(method.isTestMethod());
		setIsMethodEnabled(method.getTestMethod().getEnabled());
		setMethodName(method.getTestMethod().getMethodName());
		if (getIsTestMethod() && getIsMethodEnabled()) {
			method.getTestMethod().setRetryAnalyzer(new MethodRerunListener());
			LOGGER.info("Method " + getMethodName() + " execution starts on : " + new Date(method.getDate()).toString());
		} else {
			LOGGER.info("Method " + getMethodName() + " is not enabled");
		}
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (getIsTestMethod() && getIsMethodEnabled()) {
			LOGGER.info("Method" + getMethodName() + " execution completed on : " + new Date(method.getDate()).toString());
		}
	}

	public Boolean getIsTestMethod() {
		return isTestMethod;
	}

	public void setIsTestMethod(Boolean isTestMethod) {
		this.isTestMethod = isTestMethod;
	}

	public Boolean getIsMethodEnabled() {
		return isMethodEnabled;
	}

	public void setIsMethodEnabled(Boolean isMethodEnabled) {
		this.isMethodEnabled = isMethodEnabled;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
