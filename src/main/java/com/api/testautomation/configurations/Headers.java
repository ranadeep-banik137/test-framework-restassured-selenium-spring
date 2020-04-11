package com.api.testautomation.configurations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.app.testautomation.utilities.PropertiesFileReader;

@Component
public class Headers {
	
	EnvironmentVariables env;
	
	public Headers() {
		this.env = new EnvironmentVariables();
	}
	
	public Map<String, Object> getHeaders() {
		return new PropertiesFileReader().fetchPropertyFile(System.getProperty("user.dir") + "\\src\\test\\resources\\" + env.getApiName() + "\\" + env.getLinkName() + "\\" + "Headers.properties").fetchAll();
	}

}
