import static com.app.testautomation.initiators.SystemVariables.BROWSER;
import static com.app.testautomation.initiators.SystemVariables.getValue;
import static com.app.testautomation.initiators.SystemVariables.setValue;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.app.testautomation.initiators.ApplicationContextInitiator;
import com.ui.testautomation.pageobjectmodels.Covid19IndiaDashboard;
import com.ui.testautomation.pageobjectmodels.Covid19IndiaDatabaseSheet;

public class MainTest01 {
	
	public static void main(String[] albal) throws Exception {
		
		
		  DesiredCapabilities caps = new DesiredCapabilities();
		  caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		  caps.setCapability(CapabilityType.BROWSER_VERSION, "80.0.3987.163");
		 /* setValue("webdriver.chrome.driver", getValue("user.dir") +
		  "\\src\\main\\resources\\Drivers\\chromedriver.exe"); WebDriver driver = new
		  ChromeDriver(caps); driver.get("https://www.google.com");
		  driver.navigate().to("www.pornhub.com"); driver.get("www.pornhub.com");*/
		 
		  
			/*
			 * URL url = new URL("http://www.pornhub.com");
			 * //System.out.println(url.getContent());
			 * System.out.println(url.getProtocol());
			 * System.out.println(url.getAuthority());
			 * 
			 * setValue(SystemVariables.GRID_RUN, "false"); setValue("browser", "chrome");
			 * setValue(SystemVariables.GRID_URL, "http://localhost:4444/wd/hub");
			 * setValue("browserstack.username", "ranadeepbanik1");
			 * setValue("browserstack.passkey", "7fKPyAMpjzkK3x6ZbTrD");
			 * ApplicationContextInitiator init =
			 * ApplicationContextInitiator.getDefaultApplicationContextInitiator();
			 * WebDriver driver2 =
			 * init.startFactory().setDriverFactory().initiateDriver().getWebDriver();
			 * 
			 * try { driver2.get("https://www.pornhubpremium.com"); //WebDriver driver =
			 * init.startFactory().setDriverFactory().changeDriver(new
			 * OperaDriverInitiator().setCapabilities(caps));
			 * //driver.get("https://netflix.com"); //driver.quit(); } catch
			 * (WebDriverException exception) { exception.addInfo("Message",
			 * "URL that you provided has no protocol"); exception.addSuppressed(new
			 * Exception("URL Formed wrong")); System.out.println(exception.getMessage()); }
			 * 
			 * driver2.quit();
			 */
		  //setValue(SystemVariables.GRID_URL, "http://192.168.1.75:4444/wd/hub");
		  setValue(BROWSER, "chrome");
		  ApplicationContextInitiator init = ApplicationContextInitiator.getDefaultApplicationContextInitiator();
		  System.out.println(getValue("webdriver.chrome.driver"));
		  Covid19IndiaDashboard fac = (Covid19IndiaDashboard) init.getContext().getBean("covid19Dashboard");
		  fac.browseToDashboard();
		  //System.out.println(fac.getColumnNumber("CONFIRMED"));
		  //fac.validateAllStatesCaseCalculation();
		  fac.validateStateUICalculation("MAHARASHTRA");
		  fac.viewPatientDataBasePage();
		  Covid19IndiaDatabaseSheet data = (Covid19IndiaDatabaseSheet) init.getContext().getBean("covid19Datasheet");
		  data.switchToDatabaseWindow();
	}

}
