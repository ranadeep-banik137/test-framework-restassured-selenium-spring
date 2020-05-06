package com.ui.testautomation.pageobjectmodels;

import org.apache.log4j.Logger;

import com.app.testautomation.initiators.TestBaseSteps;

public class PageInitiator extends TestBaseSteps {

	private Covid19IndiaDashboard covid19Dashboard;
	private Covid19IndiaDatabaseSheet covid19Datasheet;
	private Covid19IndiaEssentialsPage covid19Essentials;
	private static final String PAGE_SOURCE = "Covid19IndiaPageInitiator";
	private static final Logger LOGGER = Logger.getLogger(PageInitiator.class);
	
	protected Covid19IndiaDashboard getCovid19Dashboard() {
		return covid19Dashboard;
	}

	protected Covid19IndiaDatabaseSheet getCovid19Datasheet() {
		return covid19Datasheet;
	}
	
	public Covid19IndiaEssentialsPage getCovid19Essentials() {
		return covid19Essentials;
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
		this.covid19Essentials = (Covid19IndiaEssentialsPage) getContext().getBean("covid19Essentials");
	}
	
	protected void browseToCovid19IndiaSite() {
		try {
			String url = "http://www.covid19India.org";
			getDriver().navigate().to(url);
			getDriverInitiator().takeScreenShot(PAGE_SOURCE);
			LOGGER.info("Navigated to : " + url);
			forceWait();
		} catch (Exception exception) {
			getDriverInitiator().takeScreenShot(PAGE_SOURCE + "_failed");
			LOGGER.error(exception.getMessage());
		}
	}

	protected void forceWait() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException interruptedException) {
			LOGGER.info(interruptedException.getCause());
		}
	}
}
