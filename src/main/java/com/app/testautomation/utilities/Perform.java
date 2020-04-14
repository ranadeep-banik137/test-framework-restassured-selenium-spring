package com.app.testautomation.utilities;

import java.util.List;

public class Perform {

	public static String[] toStringArray(List<String> stringList) {
		int listSize = stringList.size();
		String[] stringArray = new String[listSize - 1];
		for (int index = 0; index < listSize; index ++) {
			stringArray[index] = stringList.get(index);
		}
		return stringArray;
	}
	
	public static String[] addElement(String[] arr, String element) {
		String[] newArr = new String[arr.length + 1];
		for (int index = 0; index < arr.length; index ++) {
			newArr[index] = arr[index];
		}
		newArr[arr.length] = element;
		return newArr;
	}
}
