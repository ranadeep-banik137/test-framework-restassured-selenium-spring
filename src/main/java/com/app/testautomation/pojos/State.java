package com.app.testautomation.pojos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.testautomation.exceptions.MultipleValuesReturnedException;

public class State {

	private List<City> cities;
	private String stateName;
	
	public String getStateName() {
		return stateName;
	}
	
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public List<City> getCities() {
		return cities;
	}
	
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public void addCity(City city) {
		setCities(getCities() == null ? new ArrayList<>() : getCities());
		this.cities.add(city);
	}
	
	public void addCity(String city) {
		setCities(getCities() == null ? new ArrayList<>() : getCities());
		City cityInstance = new City();
		cityInstance.setCityName(city);
		this.cities.add(cityInstance);
	}
	
	public City getCity(String city) {
		City cityInstance = null;
		Iterator<City> cityIterator = getCities().iterator();
		while (cityIterator.hasNext()) {
			cityInstance = cityIterator.next();
			if (cityInstance.getCityName().equals(city)) {
				break;
			}
		}
		return cityInstance;
	}
	
	public City getCity() throws MultipleValuesReturnedException {
		if (getCities().size() > 1) {
			throw new MultipleValuesReturnedException();
		}
		return getCities().get(0);
	}
	
}
