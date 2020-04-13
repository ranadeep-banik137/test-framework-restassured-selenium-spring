package com.app.testautomation.listeners;

import java.util.logging.Logger;

import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener {

	private static final Logger LOGGER = Logger.getLogger(ExecutionListener.class.getName());

	@Override
	public void onExecutionStart() {
		LOGGER.info("Execution in progress....");
	}

	@Override
	public void onExecutionFinish() {
		LOGGER.info("Execution completed. Have a nice day. ;) ");
	}
	
}
