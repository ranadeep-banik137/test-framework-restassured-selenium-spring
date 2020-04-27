package com.app.testautomation.utilities;

import static com.app.testautomation.initiators.SystemVariables.API;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.Arrays;
import java.util.Iterator;
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
	
	public Config getConfig(String path) {
		Config config = this.configInstance;
		String[] strings = path.split("/");
		Iterator<String> iterator = Arrays.asList(strings).iterator();
		while (iterator.hasNext()) {
			config = config.getConfig(iterator.next());
		}
		return config;
 	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapper(Config config, String path) {
		Map<String, Object> resultantMap = null;
		try {
			resultantMap =  new ObjectMapper().convertValue(config.getAnyRef(path), Map.class);
		} catch (Exception exception) {
			LOGGER.info("No Path found");
			LOGGER.info(exception.getMessage());
		}
		return resultantMap;
	}
}
