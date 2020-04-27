package com.app.testautomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PropertiesFileReader {
	
	private static final Logger LOGGER = Logger.getLogger(PropertiesFileReader.class);
	
	private Properties properties;
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	private File file;
	private String path;
	
	public PropertiesFileReader() {
		properties = new Properties();
	}

	public PropertiesFileReader fetchPropertyFile(final String location) {
		this.properties.clear();
		this.setPath(location);
		try {
			this.setFile(new File(this.getPath()));
			if (getFile().isFile()) {
				this.setInputStream(new FileInputStream(this.getFile()));
				this.properties.load(this.getInputStream());
				LOGGER.info("Fetched properties file on location : " + location);
				this.inputStream.close();
			} else {
				LOGGER.info("No such file found " + location);
			}
		} catch (FileNotFoundException fileNotFoundException) {
			Logger.getLogger(fileNotFoundException.getCause().getMessage());
		} catch (IOException ioException) {
			Logger.getLogger(ioException.getCause().getMessage());
		} catch (Exception exception) {
			Logger.getLogger(exception.getCause().getMessage());
		}
		return this;
	}
	
	public Object fetchValueWithKey(final String key) {
		return this.properties.get(key);
	}
	
	public Map<String, Object> fetchAll() {
		Map<String, Object> objectMapper = new HashMap<>();
		for (Object key : this.properties.keySet()) {
			objectMapper.put(String.valueOf(key), fetchValueWithKey(String.valueOf(key)));
		}
		return objectMapper;
	}
	
	public PropertiesFileReader modify(Map<String, Object> modifyMap) {
		try {
			this.setOutputStream(new FileOutputStream(getFile()));
			modifyMap.forEach((key,value) -> { this.properties.replace(key, value); });
			LOGGER.info("Modified properties file with values" + modifyMap);
			storeProperty();
			this.outputStream.close();
		} catch (IOException exception) {
			String message = exception.getMessage();
			LOGGER.info(message);
			LOGGER.fatal(message);
		}
		return this;
	}
	
	public PropertiesFileReader modify(String key, Object value) {
		try {
			this.setOutputStream(new FileOutputStream(getFile()));
			this.properties.setProperty(key, String.valueOf(value));
			LOGGER.info("Modified properties file with key : " + key + ", Value : " + value);
			storeProperty();
			this.outputStream.close();
		} catch (IOException exception) {
			String message = exception.getMessage();
			LOGGER.info(message);
			LOGGER.fatal(message);
		}
		return this;
	}
	
	private void storeProperty() throws IOException {
		this.properties.store(getOutputStream(), "");
		LOGGER.info("Properties file on location" + getPath() + " updated");
	}

	private String getPath() {
		return path;
	}

	private void setPath(String path) {
		this.path = path;
	}

	private FileInputStream getInputStream() {
		return inputStream;
	}

	private void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public FileOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(FileOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	private File getFile() {
		return file;
	}

	private void setFile(File file) {
		this.file = file;
	}
}
