package com.app.testautomation.configurations;

import static com.app.testautomation.initiators.SystemVariables.OVERWRITE_LOG4J_FILE;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.testautomation.utilities.PropertiesFileReader;

@Component(value = "log4j")
public class Log4jConfiguration {
	
	private static final String LOG4J_PROPERTIES_PATH = getValue("user.dir") + "/log4j.properties";
	private PropertiesFileReader propertiesFileReader;
	private Boolean overwriteLog4jFile;
	private static final Logger LOGGER = Logger.getLogger(Log4jConfiguration.class);
	
	@Autowired
	public Log4jConfiguration(PropertiesFileReader propertiesFileReader) {
		PropertyConfigurator.configure(LOG4J_PROPERTIES_PATH);
		this.propertiesFileReader = propertiesFileReader.fetchPropertyFile(LOG4J_PROPERTIES_PATH);
		this.overwriteLog4jFile = Boolean.getBoolean(OVERWRITE_LOG4J_FILE);
		setDefaultConfigurationsBasedOnInputs();
	}
	
	public void setDefaultConfigurationsBasedOnInputs() {
		LOGGER.info("System value 'log4j.overwrite' : " + Boolean.getBoolean(OVERWRITE_LOG4J_FILE));
		this.propertiesFileReader.modify("log4j.appender.HTML_FILE.Append", String.valueOf(this.overwriteLog4jFile));
		this.propertiesFileReader.modify("log4j.appender.FILE.Append", String.valueOf(this.overwriteLog4jFile));
	}
	
}
