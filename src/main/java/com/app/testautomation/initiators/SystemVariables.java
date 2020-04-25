package com.app.testautomation.initiators;

public class SystemVariables {
	
	public static final String GRID_RUN = "grid.run";
	public static final String BROWSERSTACK_RUN = "browserstack.run";
	public static final String BROWSERSTACK_USERNAME = "browserstack.username";
	public static final String BROWSERSTACK_PASSKEY = "browserstack.passkey";
	public static final String GRID_URL = "broswerstack.url";
	public static final String BROWSER = "browser";
	public static final String OVERWRITE_LOG4J_FILE = "log4j.overwrite";
	public static final String USER_DIR = "user.dir";
	public static final String GROUPS = "groups.include";
	public static final String API = "api";
	public static final String LINK = "link";
	
	public static String getValue(String key) {
		return System.getenv(key) == null ? System.getProperty(key) : System.getenv(key);
	}
	
	public static void setValue(String key, String value) {
		System.setProperty(key, value);
	}
	
	public static String getSystemDir() {
		return System.getProperty("user.dir");
	}
}
