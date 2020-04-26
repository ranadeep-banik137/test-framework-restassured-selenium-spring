package com.api.testautomation;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.testautomation.configurations.Headers;
import com.app.testautomation.utilities.ConfigReader;
import com.app.testautomation.utilities.PropertiesFileReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CovidIndiaApiTest {
	
	public ConfigReader configInstance;
	Headers headers;
	
	@BeforeMethod(firstTimeOnly = true)
	public void initiation(Method method) {
		configInstance = new ConfigReader().initiateConfig();
		RestAssured.baseURI = String.valueOf(configInstance.getMapper(getValue(API)).get("base-uri"));
		RestAssured.useRelaxedHTTPSValidation();
	}
	
	@BeforeMethod
	public void zinitiate() {
		System.out.println("Amar Matha");
	}
	
	//@Test
	public void TestCovidDataReportTest() {
		RestAssured.basePath = String.valueOf(configInstance.readLinkData(getValue(LINK)).get("base-path"));
		RestAssured.given().
			accept(ContentType.JSON).
			headers(new Headers().setDefaultHeaders().getHeaders()).
			get().
			then().
			assertThat().
			log().
			all().
			statusCode(HttpStatus.SC_OK);
	}
	
	@Test(dataProvider = "testKarle")
	public void TestStateDistrictWiseReportTest(int i, int j, int k) {
		RestAssured.basePath = String.valueOf(configInstance.readLinkData(getValue(LINK)).get("base-path"));
		Response response = RestAssured.given().
			accept(ContentType.JSON).
			headers(new Headers().setDefaultHeaders().getHeaders()).
			get().
			then().
			assertThat().
			log().
			all().
			statusCode(HttpStatus.SC_OK).body("Maharashtra.districtData.Pune.confirmed", greaterThan(28))
			//.body("Maharashtra.districtData.findAll { it.%s == 'Unknown' }.confirmed", greaterThan(2))
			.extract().response();
		
		System.out.println("i + j + k : "  + i+j+k);
		
		Assert.assertTrue(JsonPath.from(response.asString()).getInt("Maharashtra.districtData.Pune.confirmed") > 27);
		List<Integer> sq = JsonPath.from(response.asString()).setRoot("Maharashtra.districtData*").getList("confirmed");
		System.out.println("Count :" + sq);
	}
	
	
	@Test
	public void testJson() {
		JsonPath json = JsonPath.from(System.getProperty("user.dir") + "\\src\\test\\resources\\" + getValue(API) + "\\" + getValue(LINK) + "\\" + "Request.properties");
		
	}
	
	@DataProvider(name = "testKarle")
	public Object[][] getData() {
		return new Integer[][] {{1,2,3},{2,3,4}};
	}
	
}
