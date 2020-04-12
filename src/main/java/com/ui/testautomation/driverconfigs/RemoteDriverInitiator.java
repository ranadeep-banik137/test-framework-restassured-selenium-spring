package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;

@Component(value = "remote")
public class RemoteDriverInitiator implements DriverFactory {

	private static final Logger LOGGER = Logger.getLogger(RemoteDriverInitiator.class);
	
	private DesiredCapabilities capabilities;
	private URL url;
	
	public RemoteDriverInitiator() throws MalformedURLException {
		String urlProvided = (getValue(BROWSERSTACK_USERNAME) == null && getValue(BROWSERSTACK_PASSKEY) == null) ? 
				getValue(GRID_URL) : String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", getValue(BROWSERSTACK_USERNAME), getValue(BROWSERSTACK_PASSKEY));
		if (urlProvided != null) {
			LOGGER.info(urlProvided.contains("browserstack")
					? "Browserstack driver to start with credentials USERNAME : "
							+ getValue(BROWSERSTACK_USERNAME) + ", PASSKEY : "
							+ getValue(BROWSERSTACK_PASSKEY)
					: "Grid Run to initiate. Grid Node : " + getValue(GRID_URL));
			this.setUrl(new URL(urlProvided));
			setDefaultBrowserSettings();
		}	
	}
	

	@Override
	public DriverFactory setCapabilities(DesiredCapabilities capabilities) {
		this.capabilities = capabilities == null ? new DesiredCapabilities() : capabilities;
		return this;
	}

	@Override
	public DriverFactory setExternalCapabilities(String key, Object value) throws BrowserCapabilitiesNotInitiatedException {
		if (this.capabilities == null) {
			throw new BrowserCapabilitiesNotInitiatedException();
		}
		this.capabilities.setCapability(key, value);
		return this;
	}
	
	public DesiredCapabilities getCapabilities() {
		return this.capabilities;
	}

	@Override 
	public DriverFactory setExternalCapabilities(DesiredCapabilities capabilities) throws BrowserCapabilitiesNotInitiatedException {
		if (this.capabilities == null) {
			throw new BrowserCapabilitiesNotInitiatedException();
		}
		this.capabilities.merge(capabilities);
		return this;
	}

	@Override
	public void setDefaultBrowserSettings() {
		setCapabilities(new DesiredCapabilities());
		this.capabilities.setVersion(getValue("os.version"));
		this.capabilities.setPlatform(Platform.WIN10);
		this.capabilities.setBrowserName(BROWSER);
		if (getUrl() != null) {
			switch (getValue(BROWSER)) {
			
			case "chrome" :
				this.capabilities.merge(DesiredCapabilities.chrome());
				LOGGER.info("Browser : GOOGLE CHROME");
				break;
			
			case "firefox" :
				this.capabilities.merge(DesiredCapabilities.firefox());
				LOGGER.info("Browser : MOZILLA FIREFOX");
				break;
			
			case "ie" :
				this.capabilities.merge(DesiredCapabilities.internetExplorer());
				LOGGER.info("Browser : INTERNET EXPLORER");
				break;
			
			case "opera" :
				this.capabilities.merge(DesiredCapabilities.operaBlink());
				LOGGER.info("Browser : OPERA");
				break;
			
			default :
				//throw new NoBrowserInitiatedException();
			
			}
		}
	}


	@Override
	public WebDriver initiateDriver() {
		return new RemoteWebDriver(getUrl(), getCapabilities());
	}


	public URL getUrl() {
		return url;
	}


	public void setUrl(URL url) {
		this.url = url;
	}

}
