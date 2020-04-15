package com.app.testautomation.listeners;

import static com.app.testautomation.initiators.SystemVariables.GROUPS;
import static com.app.testautomation.initiators.SystemVariables.USER_DIR;
import static com.app.testautomation.initiators.SystemVariables.getValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.app.testautomation.utilities.FileUtils;
import com.app.testautomation.utilities.ReportGenerator;
import com.relevantcodes.extentreports.LogStatus;

@Component
public class BasicTestListeners implements ISuiteListener, ITestListener {
	
	private static final Logger LOGGER = Logger.getLogger(BasicTestListeners.class.getName());
	private ReportGenerator reportGenerator;
	
	public BasicTestListeners() {
		this.reportGenerator = new ReportGenerator();
	}

	@Override
	public void onStart(ISuite suite) {
		this.reportGenerator.initiate().startSuiteTest(suite.getName());
		suite.addListener(new MethodInterceptorListener());
		suite.addListener(new MethodInvokeListener());
		suite.addListener(new AnnotationTransformerListener());
		LOGGER.info("Suite started :" + suite.getName());
	}

	@Override
	public void onFinish(ISuite suite) {
		List<ITestNGMethod> invokedMethods = getValue(GROUPS) == null ? suite.getAllMethods() : suite.getMethodsByGroups().get(getValue(GROUPS)).stream().collect(Collectors.toList());
		Iterator<ITestNGMethod> iterator = invokedMethods.iterator();
		List<String> methods = new ArrayList<>();
		while (iterator.hasNext()) {
			methods.add(iterator.next().getMethodName());
		}
		Map<String, String> fileMapper = FileUtils.getFilesMatching(getValue(USER_DIR) + "/src/test/resources/"+ getValue("api") + "/videos", methods);
		fileMapper.forEach((x,y)-> {
			this.reportGenerator.logWithVideo(this.reportGenerator.getExtentTest(), LogStatus.INFO, y, "Video clip of execution method " + x);
		});
		//this.reportGenerator.logWithVideo(this.reportGenerator.getExtentTest(), LogStatus.INFO, getValue(USER_DIR) + "/src/main/resources/video001.mp4", "Video of the test");
		this.reportGenerator.endReport(suite);
		LOGGER.info("Suite execution ended" + suite.getName());
		LOGGER.info("Total Methods :" + (suite.getAllMethods().size() + suite.getExcludedMethods().size()));
		LOGGER.info("Executed Successfully : " + suite.getAllMethods().size());
		LOGGER.info("No of Methods not executed : " + suite.getExcludedMethods().size());
		
	}

	@Override
	public void onTestStart(ITestResult result) {
		this.reportGenerator.setSuiteTestCategories(result.getMethod().getGroups());
		this.reportGenerator.startMethodTest(new Date(result.getMethod().getDate()), result.getName(), result.getMethod().getId().replace("main@", "Method Id - ") + " : " + result.getMethod().getDescription());
		LOGGER.info("Executiing method : " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		this.reportGenerator.log(this.reportGenerator.getMethodTest(), LogStatus.PASS, result.getMethod().getDescription()).endTest(result);
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		this.reportGenerator.log(this.reportGenerator.getMethodTest(), LogStatus.FAIL, result.getMethod().getDescription()).endTest(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		this.reportGenerator.log(this.reportGenerator.getMethodTest(), LogStatus.SKIP, result.getMethod().getDescription()).endTest(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(ITestContext context) {
		LOGGER.info("Test started at : " + context.getStartDate().toGMTString());
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onFinish(ITestContext context) {
		LOGGER.info(context.getHost());
		LOGGER.info(context.getName());
		LOGGER.info(context.getOutputDirectory());
		LOGGER.info("Test ended at : " + context.getEndDate().toGMTString());
		
	}

	
}
