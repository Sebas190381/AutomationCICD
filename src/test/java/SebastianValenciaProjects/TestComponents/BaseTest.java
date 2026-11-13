package SebastianValenciaProjects.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import SebastianValenciaProjects.pageObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

// Fix Warning 1: install the JUL→SLF4J bridge so Selenium's Java Util Logging
// warnings (e.g. CDP version mismatch) are routed through SLF4J and suppressed
// by the log levels defined in simplelogger.properties
import org.slf4j.bridge.SLF4JBridgeHandler;

public class BaseTest {

	public WebDriver driver;
	public LandingPage landingPage;

	// Static initializer: runs once when the class is loaded.
	// Removes the default JUL console handler and installs the SLF4J bridge
	// so all java.util.logging output is controlled by simplelogger.properties.
	static {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}




	public WebDriver initializeDriver() throws IOException {

		//properties class

		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") 
			    + "/src/main/java/SebastianValenciaProjects/Resources/GlobalData.properties");
		properties.load(fis);
		String browserProp = System.getProperty("browser")!=null ? System.getProperty("browser"): properties.getProperty("browser");
		//String browserProp = properties.getProperty("browser");

		if(browserProp.equalsIgnoreCase("chrome") || browserProp.equalsIgnoreCase("chromeheadless")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();

			if(browserProp.contains("headless")) {
				options.addArguments("--headless");
 			}
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1440,900));

		}else if(browserProp.equalsIgnoreCase("firefox") || browserProp.equalsIgnoreCase("firefoxheadless")) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			if(browserProp.contains("headless")) {
				options.addArguments("-headless");
			}
			driver = new FirefoxDriver(options);

		}else if(browserProp.equalsIgnoreCase("edge") || browserProp.equalsIgnoreCase("edgeheadless")) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			if(browserProp.contains("headless")) {
				options.addArguments("--headless");
			}
			driver = new EdgeDriver(options);

		}else if(browserProp.equalsIgnoreCase("opera") || browserProp.equalsIgnoreCase("operaheadless")) {
			// Opera is Chromium-based. Selenium 4 removed OperaDriver/OperaOptions,
			// so Opera is driven with ChromeDriver pointed at the Opera binary
			// (Selenium Manager resolves chromedriver, which drives any
			// Chromium-based browser)
			ChromeOptions options = new ChromeOptions();
			options.setBinary(System.getProperty("user.home") + "/Applications/Opera.app/Contents/MacOS/Opera");
			if(browserProp.contains("headless")) {
				options.addArguments("--headless");
			}
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1440,900));

		}
		 if (driver != null) {
	            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	            driver.manage().window().maximize();
	        }

		return driver;
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		//Read to string
		String jsonContent = FileUtils.readFileToString(new File (filePath),
				StandardCharsets.UTF_8);
		//String to hashmap jackson databind implement dependence
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>(){});
		return data;
	}


	public  String getScreenShot(String testCaseName, WebDriver driver) throws IOException{
		TakesScreenshot ts = (TakesScreenshot)driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir")+"//reports//"+testCaseName+".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir")+"//reports//"+testCaseName+".png";

 	}

	@BeforeMethod(alwaysRun = true)
	@Parameters("browser")
	public LandingPage launchApplication(@Optional("chrome") String browserParam) throws IOException{
		// Resolution order: -Dbrowser system property (CLI/CI) wins,
		// then the TestNG suite <parameter name="browser"> value,
		// then GlobalData.properties as the last fallback
		if (System.getProperty("browser") == null && browserParam != null) {
			System.setProperty("browser", browserParam);
		}
		driver = initializeDriver();
		landingPage = new LandingPage(driver);
		landingPage.goTo();
		return landingPage;
	}

	 @AfterMethod(alwaysRun = true)
	 public void tearDown() {
		 driver.close();

	 }
}
