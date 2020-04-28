package com.app.testautomation.utilities;

import static com.app.testautomation.initiators.SystemVariables.USER_DIR;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Component(value = "report")
public class ReportGenerator {

	private static final Logger LOGGER = Logger.getLogger(ReportGenerator.class);
	private static final String REPORT_FILE = getValue(USER_DIR) + "/src/main/resources/reports/" + getValue("api") + "AutomationReport.html";
	private ExtentReports extentReport;
	private ExtentTest extentTest;
	private ExtentTest methodTest;
	
	public ReportGenerator() {
		this.setExtentReport(new ExtentReports(REPORT_FILE, true));
		LOGGER.info("Report to be generated at location : " + REPORT_FILE);
	}
	
	public ReportGenerator generateReportOnNewLocation(String path) {
		this.setExtentReport(new ExtentReports(path, true));
		LOGGER.info("REPORT Location changed to : " + path);
		return this;
	}

	public ExtentReports getExtentReport() {
		return extentReport;
	}

	public void setExtentReport(ExtentReports extentReport) {
		this.extentReport = extentReport;
		LOGGER.info("Extent Report generated");
		LOGGER.info("Report ID : " + this.extentReport.getReportId());
	}
	
	public ReportGenerator initiate() {
		this.extentReport.addSystemInfo("Admin Name", "RANADEEP BANIK");
		this.extentReport.assignProject("RDB's FRAMEWORK : " + getValue("api") + " Test");
		return this;
	}
	
	public ReportGenerator startSuiteTest(String testDescription) {
		setExtentTest(this.extentReport.startTest(getValue("api") + " UI Test", "All test cases performed on " + getValue("api") + " website"));
		this.extentTest.assignAuthor(getValue("user.name"));
		this.extentTest.setDescription(testDescription);
		return this;
	}
	
	public ReportGenerator setSuiteTestCategories(String[] categories) {
		this.extentTest.addScreenCapture("D:/Docs/Press/BigTitsBoss - Sophia Lomeli/pic.jpg");
		this.extentTest.addScreencast("D:/Docs/Press/BigTitsBoss - Sophia Lomeli/pic.jpg");
		this.extentTest.assignCategory(categories);
		return this;
	}
	
	public ReportGenerator startMethodTest(Date date, String methodName, String methodDescription) {
		ExtentTest test = new ExtentTest(methodName, methodDescription == null ? "" : methodDescription);
		setMethodTest(test);
		this.methodTest.setDescription(methodDescription);
		this.extentTest.appendChild(getMethodTest());
		this.methodTest.setStartedTime(date);
		return this;
	}
	
	public ReportGenerator log(ExtentTest test, LogStatus status, String steps, String details) {
		test.log(status, steps, details);
		return this;
	}
	
	public ReportGenerator log(ExtentTest test, LogStatus status, String details) {
		if (status.equals(LogStatus.FAIL)) {
			String[] failedScreens = FileUtils.getFilesMatching(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/shots", "_failed");
			if (failedScreens.length > 0) {
				for (String screen : failedScreens) {
					test.log(status, test.addScreenCapture(screen), "Adding the screenshot " + screen + " as the test failed");
				}
			}
		}
		test.log(status, "Method Name : " + test.getDescription(), details);
		return this;
	}
	
	public ReportGenerator logWithScreenShot(ExtentTest test, LogStatus status, String filePath, String details) {
		test.log(status, test.addScreenCapture(filePath), details);
		return this;
	}
	
	public ReportGenerator logWithVideo(ExtentTest test, LogStatus status, String filePath, String details) {
		test.log(status, test.addScreencast(filePath), details);
		return this;
	}
	
	public void setExtentTest(ExtentTest test) {
		this.extentTest = test;
	}
	
	public ExtentTest getExtentTest() {
		return this.extentTest;
	}
	
	public ReportGenerator endTest(ITestResult result) {
		this.extentReport.endTest(getExtentTest());
		return this;
	}
	
	public void endReport(ISuite suite) {
		this.extentReport.flush();
		this.extentReport.close();
		LOGGER.info("Suite execution ended" + suite.getName());
		LOGGER.info("Total Methods :" + suite.getAllMethods().size());
		LOGGER.info("Executed Successfully : " + suite.getAllInvokedMethods().size());
		LOGGER.info("No of Methods not executed : " + suite.getExcludedMethods().size());
	}

	public ExtentTest getMethodTest() {
		return methodTest;
	}

	public void setMethodTest(ExtentTest methodTest) {
		this.methodTest = methodTest;
	}
}
