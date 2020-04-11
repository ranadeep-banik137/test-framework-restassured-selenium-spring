package com.api.testautomation.configurations;

import org.springframework.stereotype.Component;

@Component
public class EnvironmentVariables {
	
	public String api;
	public String link;
	
	{
		api = System.getenv("api") == null ? System.getProperty("api") : System.getenv("api");
		link = System.getenv("link") == null ? System.getProperty("link") : System.getenv("link");
	}
	
	public String getApiName() {
		return this.api;
	}
	
	public String getLinkName() {
		return this.link;
	}

}
