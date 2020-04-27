package com.api.testautomation.configurations;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.testautomation.utilities.ConfigReader;
import com.app.testautomation.utilities.PropertiesFileReader;

import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;

@Component
public class Headers {
	
	private static final Logger LOGGER = Logger.getLogger(Headers.class);
	
	private Map<String, Object> headerMapper;
	@Autowired
	private PropertiesFileReader propertiesFileReader;
	@Autowired
	private ConfigReader configReader;
	
	public Headers() {
		this.headerMapper = new HashMap<>();
	}
	
	public Headers setDefaultHeaders() {
		this.configReader.initiateConfig();
		Map<String, Object> defaultHeadersFromConfig = this.configReader.getMapper(this.configReader.getConfig(getValue(API)), "common-headers");
		Map<String, Object> headersFromConfig = this.configReader.getMapper(this.configReader.getConfig(getValue(API) + "/" + getValue(LINK)), "headers");
		Map<String, Object> headersFromProperties = propertiesFileReader.fetchPropertyFile(System.getProperty("user.dir") + "\\src\\test\\resources\\" + getValue(API) + "\\" + getValue(LINK) + "\\" + "Headers.properties").fetchAll();
		if (defaultHeadersFromConfig != null) 
			this.headerMapper.putAll(defaultHeadersFromConfig);
		if (headersFromConfig != null)
			this.headerMapper.putAll(headersFromConfig);
		if (headersFromProperties != null)
			this.headerMapper.putAll(headersFromProperties);
		LOGGER.info("Headers : " + this.headerMapper);
		setHeaderMapper(this.headerMapper);
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
		LOGGER.info("Headers added explicitly :" + headerMapper);
		return this;
	}
	
	public Headers setExplicitHeaders(String key, String value) {
		setDefaultHeaders();
		this.headerMapper.put(key, value);
		LOGGER.info("Headers added explicitly :" + key + " : "+  value);
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
