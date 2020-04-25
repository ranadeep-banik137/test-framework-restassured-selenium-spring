package com.api.testautomation.configurations;

public enum Links {

	DATA_REPORT("data-report"), 
	STATE_DISTRICT("state-district"), 
	STATE_DISTRICT_V2("state-district-v2"), 
	TRAVEL_HISTORY("travel-history"), 
	RAW_DATA("raw-data"), 
	STATE_DAILY_CHANGES("states-daily-changes"), 
	DEATHS_AND_RECOVERY("deaths-and-recovery"), 
	ESSENTIALS("essentials-resources");
	
	private String value;

	Links(String value) {
		this.value = value;
	}

	public String getLink() {
		return value;
	}
}
