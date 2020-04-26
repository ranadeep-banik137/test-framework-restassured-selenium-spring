package com.api.testautomation.configurations;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.app.testautomation.utilities.PropertiesFileReader;

import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;

@Component
public class Headers {
	
	private Map<String, Object> headerMapper;
	@Autowired
	private PropertiesFileReader propertiesFileReader;
	
	public Headers() {
		this.headerMapper = new HashMap<>();
	}
	
	public Headers setDefaultHeaders() {
		setHeaderMapper(propertiesFileReader.fetchPropertyFile(System.getProperty("user.dir") + "\\src\\test\\resources\\" + getValue(API) + "\\" + getValue(LINK) + "\\" + "Headers.properties").fetchAll());
		return this;
	}
	
	public Map<String, Object> getHeaders() {
		return this.headerMapper;
	}
	
	public void setHeaderMapper(Map<String, Object> headerMapper) {
		this.headerMapper = headerMapper;
	}
	
	public Headers setExplicitHeaders(Map<String, Object> headerMapper) {
		setDefaultHeaders();
		this.headerMapper.putAll(headerMapper);
		return this;
	}
	
	public Headers setExplicitHeaders(String key, String value) {
		setDefaultHeaders();
		this.headerMapper.put(key, value);
		return this;
	}
	
	public Headers set(String key, String value) {
		this.headerMapper.put(key, value);
		return this;
	}
	
	public Headers set(Map<String, Object> headerMapper) {
		this.headerMapper.putAll(headerMapper);
		return this;
	}
	
	public Headers setHeaderConfig(String key, String value) {
		RestAssured.config().headerConfig(new HeaderConfig().overwriteHeadersWithName(key, value));
		return this;
	}
	
	public Headers remove(String key) {
		this.headerMapper.remove(key);
		return this;
	}
	

}
