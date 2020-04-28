package com.ui.testautomation.pageobjectmodels;

import com.app.testautomation.initiators.TestBaseSteps;

public class PageInitiator extends TestBaseSteps {

	private Covid19IndiaDashboard covid19Dashboard;
	private Covid19IndiaDatabaseSheet covid19Datasheet;
	
	protected Covid19IndiaDashboard getCovid19Dashboard() {
		return covid19Dashboard;
	}

	protected Covid19IndiaDatabaseSheet getCovid19Datasheet() {
		return covid19Datasheet;
	}
	
	public PageInitiator() {
		pageConstruct();
	}
	
	protected void initiateDriver() {
		if (!getDriverInitiator().isDriverInitiated()) {
			super.construct();
			pageConstruct();
		}
	}
	
	private void pageConstruct() {
		this.covid19Dashboard = (Covid19IndiaDashboard) getContext().getBean("covid19Dashboard");
		this.covid19Datasheet = (Covid19IndiaDatabaseSheet) getContext().getBean("covid19Datasheet");
	}
}
