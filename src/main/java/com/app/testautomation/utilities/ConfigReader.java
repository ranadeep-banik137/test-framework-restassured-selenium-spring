package com.app.testautomation.utilities;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Component
public class ConfigReader {
	
	private final static String fileName = "apimappers.conf";
	private static final Logger LOGGER = Logger.getLogger(ConfigReader.class);
	
	public Config configInstance;

	public ConfigReader initiateConfig() {
		this.configInstance =  ConfigFactory.load(fileName);
		LOGGER.info("Config file initiated");
		return this;
	}
	
	public Config getApiConfigDetails() {
		return this.configInstance.getConfig(getValue(API));
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapper(String link) {
		LOGGER.info("Fetching config property of : " + link);
		return new ObjectMapper().convertValue(this.configInstance.getAnyRef(link), Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> readLinkData(String link) {
		LOGGER.info("Fetching config property of : " + link);
		return new ObjectMapper().convertValue(this.configInstance.getConfig(getValue(API)).getAnyRef(link), Map.class);
	}
}
