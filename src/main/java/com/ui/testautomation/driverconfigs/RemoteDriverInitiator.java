package com.ui.testautomation.driverconfigs;

import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.BrowserCapabilitiesNotInitiatedException;
import com.app.testautomation.factory.DriverFactory;
import com.app.testautomation.initiators.SystemVariables;

@Component(value = "remote")
public class RemoteDriverInitiator implements DriverFactory {

	private DesiredCapabilities capabilities;
	private URL url;
	
	public RemoteDriverInitiator() throws MalformedURLException {
		String urlProvided = (getValue(SystemVariables.BROWSERSTACK_USERNAME) == null && getValue(SystemVariables.BROWSERSTACK_PASSKEY) == null) ? 
				getValue(SystemVariables.GRID_URL) : String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", getValue(SystemVariables.BROWSERSTACK_USERNAME), getValue(SystemVariables.BROWSERSTACK_PASSKEY));
		if (urlProvided != null) {
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
		this.capabilities.setBrowserName(SystemVariables.BROWSER);
		if (getUrl() != null) {
			switch (getValue(SystemVariables.BROWSER)) {
			
			case "chrome" : {
				this.capabilities.merge(DesiredCapabilities.chrome());
				break;
			}
			
			case "firefox" : {
				this.capabilities.merge(DesiredCapabilities.firefox());
				break;
			}
			
			case "ie" : {
				this.capabilities.merge(DesiredCapabilities.internetExplorer());
				break;
			}
			
			case "opera" : {
				this.capabilities.merge(DesiredCapabilities.operaBlink());
				break;
			}
			
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
