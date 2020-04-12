package com.app.testautomation.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

/**
 * This interface provides all the necessary methods to initiate required driver
 * properties/capabilities along with addition of capabilities required
 * situationally
 */
@Component(value = "driverFactory")
public interface DriverFactory {
	
	/**
	 * Set default browser settings such as: Eg : for firefox, it initiates Firefox
	 * Profile instance and also adds required functionalities
	 */
	public void setDefaultBrowserSettings();
	
	/**
	 * Changes the capabilities instance completely (initiates the intance with
	 * capabilities instance provided as parameter
	 * 
	 * @param capabilities
	 * @return this
	 */
	public DriverFactory setCapabilities(DesiredCapabilities capabilities);
	
	/**
	 * Adds capabilities to the existing capabilities instance
	 * @param key
	 * @param value
	 * @return this
	 * @throws Exception
	 */
	public DriverFactory setExternalCapabilities(String key, Object value) throws Exception;
	
	/**
	 * Adds capabilities to the existing capabilities instance
	 * @param capabilities
	 * @return
	 * @throws Exception
	 */
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) throws Exception;
	
	/**
	 * Initiates the WebDriver instance (based on which browser instance provided by the client)
	 * @return WebDriver preffered
	 */
	public WebDriver initiateDriver();
	
}
