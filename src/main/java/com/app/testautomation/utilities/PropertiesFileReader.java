package com.app.testautomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class PropertiesFileReader {
	
	private Properties properties;
	private FileInputStream inputStream;
	private File file;
	private String path;
	
	public PropertiesFileReader() {
		properties = new Properties();
	}

	public PropertiesFileReader fetchPropertyFile(final String location) {
		this.path = location;
		this.file = new File(this.path);
		try {
			this.inputStream = new FileInputStream(this.file);
			this.properties.load(this.inputStream);
		} catch (FileNotFoundException fileNotFoundException) {
			Logger.getLogger(fileNotFoundException.getCause().getMessage());
		} catch (IOException ioException) {
			Logger.getLogger(ioException.getCause().getMessage());
		}
		return this;
	}
	
	public Object fetchValueWithKey(final String key) {
		return this.properties.get(key);
	}
	
	public Map<String, Object> fetchAll() {
		Map<String, Object> objectMapper = new HashMap<String, Object>();
		for (Object key : this.properties.keySet()) {
			objectMapper.put(String.valueOf(key), fetchValueWithKey(String.valueOf(key)));
		}
		return objectMapper;
	}
}
