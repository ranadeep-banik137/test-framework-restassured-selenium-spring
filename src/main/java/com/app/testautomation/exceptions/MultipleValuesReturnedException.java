package com.app.testautomation.exceptions;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class MultipleValuesReturnedException extends Exception {
	
	/**
	 * This exception is created to catch situations where user expects single value
	 * to return from a list instead having multiple values assigned to it
	 */
	private static final long serialVersionUID = 1484996086659328631L;

	@SuppressWarnings("deprecation")
	public MultipleValuesReturnedException() {
		String message = "Return value list having multiple elements, So single element cannot be returned";
		Logger.getLogger(MultipleValuesReturnedException.class).log(Priority.WARN, message);
	}
	
	public MultipleValuesReturnedException(String message) {
		super(message);
	}
}
