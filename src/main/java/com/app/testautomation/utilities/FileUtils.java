package com.app.testautomation.utilities;

import static com.app.testautomation.initiators.SystemVariables.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {

	//private String location;
	
	public static String[] getFilesMatching(String location, String fileNamePart) {
		File path = new File(location);
		File[] allFiles = path.listFiles();
		List<String> resultantFileNames = new ArrayList<>();
		for (File each : allFiles) {
			if (each.getAbsolutePath().contains(fileNamePart)) {
				resultantFileNames.add(each.getAbsolutePath());
			}
		}
		return Perform.toStringArray(resultantFileNames);
	}
	
	public static Map<String, String> getFilesMatching(String location, List<String> fileNameParts) {
		Map<String, String> resultantFileMap = new HashMap<>();
		File path = new File(location);
		File[] allFiles = path.listFiles();
		//List<String> resultantFileNames = new ArrayList<>();
		Iterator<String> iterator = fileNameParts.iterator();
		while (iterator.hasNext()) {
			String fileNamePart = iterator.next();
			for (File each : allFiles) {
				if (each.getAbsolutePath().contains(fileNamePart)) {
					resultantFileMap.put(fileNamePart, each.getAbsolutePath());
				}
			}
		}
		return resultantFileMap;
	}
	
	public static void flushFilesFromClassPath(String directory) {
		flushFiles(getValue(USER_DIR) + "/" + directory);
	}
	
	public static void flushFiles(String directory) {
		File path = new File(directory);
		if (path.isDirectory() == false) {
			return;
		} else {
			File[] files = path.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}
}

