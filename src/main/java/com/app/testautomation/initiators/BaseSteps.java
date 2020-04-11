package com.app.testautomation.initiators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ui.testautomation.pageobjectmodels.Covid19IndiaDashboard;
import com.ui.testautomation.pageobjectmodels.Covid19IndiaDatabaseSheet;

@Component
public class BaseSteps {

	@Autowired
	protected Covid19IndiaDashboard covid19Dashboard;
	@Autowired
	protected Covid19IndiaDatabaseSheet covid19Datasheet;
	
}
