package com.app.testautomation.utilities;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.api.testautomation.configurations.EnvironmentVariables;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Component
public class ConfigReader {
	
	private final static String fileName = "apimappers.conf";
	public Config configInstance;
	public String apiName;
	EnvironmentVariables env;
	
	public ConfigReader() {
		this.env = new EnvironmentVariables();
		apiName = env.getApiName();
	}

	public ConfigReader initiateConfig() {
		this.configInstance =  ConfigFactory.load(fileName);
		return this;
	}
	
	public Config getApiConfigDetails() {
		return this.configInstance.getConfig(apiName);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapper(String link) {
		return new ObjectMapper().convertValue(this.configInstance.getAnyRef(link), Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> readLinkData(String link) {
		return new ObjectMapper().convertValue(this.configInstance.getConfig(apiName).getAnyRef(link), Map.class);
	}
}
