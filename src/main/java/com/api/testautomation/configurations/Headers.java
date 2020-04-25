package com.api.testautomation.configurations;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.app.testautomation.utilities.PropertiesFileReader;

import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;

@Component
public class Headers {
	
	private Map<String, String> headerMapper;
	
	public Headers() {
		this.headerMapper = new HashMap<>();
	}
	
	public Map<String, Object> getHeaders() {
		return new PropertiesFileReader().fetchPropertyFile(System.getProperty("user.dir") + "\\src\\test\\resources\\" + getValue(API) + "\\" + getValue(LINK) + "\\" + "Headers.properties").fetchAll();
	}
	
	public Map<String, String> getExplicitHeaders() {
		return this.headerMapper;
	}
	
	public Headers set(Map<String, String> headerMapper) {
		this.headerMapper.putAll(headerMapper);
		return this;
	}
	
	public Headers set(String key, String value) {
		this.headerMapper.put(key, value);
		return this;
	}
	
	public Headers setHeaderConfig(String key, String value) {
		RestAssured.config().headerConfig(new HeaderConfig().overwriteHeadersWithName(key, value));
		return this;
	}
	
	

}
