package com.app.testautomation.utilities;

import java.util.List;

public class Perform {
	
	private static int result = 0;

	public static String[] toStringArray(List<String> stringList) {
		int listSize = stringList.size();
		String[] stringArray = new String[listSize];
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
	
	public static void sumOf(String... s) {
		flushCount();
		for (String v : s) {
			result += Integer.parseInt(v);
		}
	}
	
	public static void sumOf(List<Object> s) {
		flushCount();
		for (Object v : s) {
			result += Integer.parseInt(String.valueOf(v));
		}
	}
	
	private static void flushCount() {
		result = 0;
	}
	
	public static int getCount() {
		int returnResult = result;
		flushCount();
		return returnResult;
	}
}
