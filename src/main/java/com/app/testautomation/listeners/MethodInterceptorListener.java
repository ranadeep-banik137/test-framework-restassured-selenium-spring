package com.app.testautomation.listeners;

import static com.app.testautomation.initiators.SystemVariables.GROUPS;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.app.testautomation.utilities.Perform;

public class MethodInterceptorListener implements IMethodInterceptor {

	private static final Logger LOGGER = Logger.getLogger(MethodInterceptorListener.class.getName());
private String systemDefined;
	
	public MethodInterceptorListener() {
		this.systemDefined = getValue(GROUPS) == null ? "default" : getValue(GROUPS);
	}

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<IMethodInstance> refreshedMethods = new ArrayList<>();
		LOGGER.info("Intercepting methods as per group name provided");
		for (IMethodInstance method : methods) {
			Test testMethod = method.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
			LOGGER.info("As per requested groups : [" + this.systemDefined + "]");
			if (isGroupAvailable(testMethod.groups())) {
				refreshedMethods.add(method);
				LOGGER.info(method.getMethod().getMethodName() + " to be executed");
			}
		}
		return refreshedMethods;
	}
	
	private Boolean isGroupAvailable(String[] groups) {
		String[] systemgroups = systemDefined.split(",");
		List<String> systemDefinedGroupList = new ArrayList<>();
		for (int index = 0; index < systemgroups.length; index++) {
			systemDefinedGroupList.add(systemgroups[index].trim());
		}
		List<String> testDefinedGroupList = Arrays.asList(Perform.addElement(groups, "default"));
		return ((systemDefinedGroupList.size() == 0) || (testDefinedGroupList.size() == 0)) ? true : testDefinedGroupList.containsAll(systemDefinedGroupList);
	}
	
}
