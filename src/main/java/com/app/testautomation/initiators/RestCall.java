package com.app.testautomation.initiators;

import static com.app.testautomation.initiators.SystemVariables.API;
import static com.app.testautomation.initiators.SystemVariables.LINK;
import static com.app.testautomation.initiators.SystemVariables.getValue;
import static com.app.testautomation.initiators.SystemVariables.setValue;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.testautomation.configurations.Headers;
import com.api.testautomation.configurations.Links;
import com.app.testautomation.utilities.ConfigReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Component(value = "rest")
public class RestCall {
	
	private static final Logger LOGGER = Logger.getLogger(RestCall.class);
	private String apiLink;
	@Autowired
	private Headers headers;
	@Autowired
	private ConfigReader configInstance;
	
	public RestCall() {
		LOGGER.info("Spring Bean initiation of Rest Call class");
	}
	
	public RestCall link(Links link) {
		this.apiLink = link.getLink();
		setValue(LINK, this.apiLink);
		this.headers.setDefaultHeaders();
		this.configInstance.initiateConfig();
		RestAssured.baseURI = String.valueOf(configInstance.getMapper(getValue(API)).get("base-uri"));
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.basePath = String.valueOf(configInstance.readLinkData(getValue(LINK)).get("base-path"));
		return this;
	}
	
	public RestCall explicitHeaders(Map<String, Object> headers) {
		return this;
	}
	
	public String getResponse() {
		return RestAssured.given()
			.accept(ContentType.JSON)
			.headers(this.headers.getHeaders())
			.log()
			.all()
			.get()
			.then()
			.assertThat()
			//.log()
			//.all()
			.statusCode(HttpStatus.SC_OK).extract().asString();
	}

}
