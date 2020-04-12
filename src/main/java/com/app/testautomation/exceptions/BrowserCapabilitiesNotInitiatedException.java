package com.app.testautomation.exceptions;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * This exception is created when client does not initiate/replace empty browser
 * capabilities. Which turns in not initiating any remote web driver.
 */
public class BrowserCapabilitiesNotInitiatedException extends Exception {

	/**
	 * Serial Version UID Number auto Generated
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public BrowserCapabilitiesNotInitiatedException() {
		String message = "Capabilities/Profile/Options instance has not been initiated";
		Logger.getLogger(BrowserCapabilitiesNotInitiatedException.class).log(Priority.WARN, message);
	}
	
	public BrowserCapabilitiesNotInitiatedException(String message) {
		super(message);
	}
}
