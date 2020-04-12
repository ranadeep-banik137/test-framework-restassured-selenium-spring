package com.app.testautomation.exceptions;

import org.testng.log4testng.Logger;

/**
 * This exception is created when client does not pass browser
 * parameter/property. Which turns in not initiating any remote web driver.
 */
public class NoBrowserInitiatedException extends Exception {

	/**
	 * Serial Version UID Number auto Generated
	 */
	private static final long serialVersionUID = 662395671993983112L;

	public NoBrowserInitiatedException() {
		String message = "No browser property value has been provided. Please include maven goals -Dbrowser= {Name of the browser}."
				+ "Eg : -Dbrowser = \"chrome\"";
		Logger.getLogger(NoBrowserInitiatedException.class).warn(message);
	}
	
	public NoBrowserInitiatedException(String message) {
		super(message);
	}
}
