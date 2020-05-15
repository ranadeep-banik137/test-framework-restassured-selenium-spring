package com.app.testautomation.utilities;

import static com.app.testautomation.initiators.SystemVariables.BROWSER;
import static com.app.testautomation.initiators.SystemVariables.USER_DIR;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.testng.ISuite;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

@Component (value = "report2")
public class ReportGenerator2 {
	
	private static final Logger LOGGER = Logger.getLogger(ReportGenerator2.class);
	private static final String REPORT_FILE = getValue(USER_DIR) + "/src/main/resources/reports/" + getValue("api") + "AventStackAutomationReport.html";
	
	private ExtentReports extentReport;
	private ExtentReporter reporter;
	private ExtentTest extentTest;
	private ExtentTest methodTest;

	public ExtentReports getExtentReport() {
		return extentReport;
	}

	public void setExtentReport(ExtentReports extentReport) {
		this.extentReport = extentReport;
	}
	
	public ReportGenerator2 initiate() {
		setExtentReport(new ExtentReports());
		try {
			
		
			getExtentReport().attachReporter(getReporter());
		} catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}
		this.extentReport.setSystemInfo("Browser", getValue(BROWSER));
		this.extentReport.setSystemInfo("Admin name", "Ranadeep Banik");
		return this;
	}
	
	public ReportGenerator2 startSuiteTest(String testName, String description) {
		setExtentTest(getExtentReport().createTest(testName,description == null ? "" : description));
		this.extentTest.assignAuthor(getValue("user.name"));
		return this;
	}
	
	public ReportGenerator2 setSuiteTestCategory(String category) {
		this.extentTest.assignCategory(category);
		return this;
	}
	
	public ReportGenerator2 setMethodTest(Date date, String methodName, String methodDescription) {
		setMethodTest(getExtentTest().createNode(methodName, methodDescription == null ? "" : methodDescription));
		return this;
	}
	
	public ReportGenerator2 log(ExtentTest test, Status status, String details) {
		ExtentColor color = null;
		String description = null;
		if (status.equals(Status.PASS)) {
			color = ExtentColor.GREEN;
			description = details + "," + "/n STATUS : PASSED";
		} else if (status.equals(Status.FAIL)) {
			color = ExtentColor.RED;
			description = details + "," + "/n STATUS : FAILED";
			String[] failedScreens = FileUtils.getFilesMatching(getValue(USER_DIR) + "/src/test/resources/" + getValue("api") + "/shots", "_failed");
			if (failedScreens.length > 0) {
				for (String screen : failedScreens) {
					try {
						screen = FileUtils.copyFileToTarget(screen);
						test.fail("ScreenShot : 'file://'" + screen, MediaEntityBuilder.createScreenCaptureFromPath(screen).build());
					} catch (IOException exception) {
						LOGGER.fatal(exception.getMessage());
					}
				}
			}
		} else {
			color = ExtentColor.GREY;
			description = details + "," + "/n STATUS : SKIPPED";
		}
		test.log(status, MarkupHelper.createLabel(description, color));
		return this;
	}
	
	public ReportGenerator2 logWithVideo(ExtentTest test, Status status, String filePath) {
		try {
			test.addScreencastFromPath(filePath).info(filePath);
		} catch (IOException exception) {
			LOGGER.info(exception.getMessage());
		}
		return this;
	}
	
	public void endReport(ISuite suite) {
		this.extentReport.flush();
		LOGGER.info("Suite execution ended" + suite.getName());
		LOGGER.info("Total Methods :" + suite.getAllMethods().size());
		LOGGER.info("Executed Successfully : " + suite.getAllInvokedMethods().size());
		LOGGER.info("No of Methods not executed : " + suite.getExcludedMethods().size());
	}
	
	public ReportGenerator2 setDefaultHtmlReporter() {
		setReporter(new ExtentHtmlReporter(new File(REPORT_FILE)));
		return this;
	}
	
	public ReportGenerator2 setHtmlReporter(String reportLocation) {
		setReporter(new ExtentHtmlReporter(new File(reportLocation)));
		return this;
	}

	private ExtentReporter getReporter() {
		return reporter;
	}

	private void setReporter(ExtentReporter reporter) {
		this.reporter = reporter;
	}

	public ExtentTest getExtentTest() {
		return extentTest;
	}

	public void setExtentTest(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	public ExtentTest getMethodTest() {
		return methodTest;
	}

	public void setMethodTest(ExtentTest methodTest) {
		this.methodTest = methodTest;
	}
}
