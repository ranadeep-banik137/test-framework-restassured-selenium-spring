package com.app.testautomation.initiators;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.api.testautomation","com.ui.testautomation", "com.app.testautomation"})
public class ContextInitiators {
}
