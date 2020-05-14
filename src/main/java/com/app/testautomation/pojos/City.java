package com.app.testautomation.pojos;

import java.util.ArrayList;
import java.util.List;

import com.app.testautomation.exceptions.MultipleValuesReturnedException;

public class City {

	private List<String> categories;
	private String cityName;
	
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public List<String> getCategories() {
		return categories;
	}
	
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public void addCategory(String category) {
		setCategories(getCategories() == null ? new ArrayList<>() : getCategories());
		this.categories.add(category);
	}

	public String getCategory() throws MultipleValuesReturnedException {
		if (getCategories().size() > 1) {
			throw new MultipleValuesReturnedException();
		}
		return getCategories().get(0);
	}
}
