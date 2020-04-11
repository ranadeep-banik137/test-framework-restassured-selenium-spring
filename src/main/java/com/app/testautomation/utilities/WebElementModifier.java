package com.app.testautomation.utilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component(value = "webElementModifier")
public class WebElementModifier {

	public WebElement appendWebElement(WebElement element, By appender) {
		return element.findElement(appender);
	}
	
	public List<WebElement> appendWebElementToList(WebElement element, By appender) {
		return element.findElements(appender);
	}
}
