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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import SebastianValenciaProjects.pageObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public WebDriver driver;
	public LandingPage landingPage;




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

		}else if(browserProp.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			 driver = new FirefoxDriver();


		}else if(browserProp.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "edge.exe");

			 driver = new EdgeDriver();

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
	public LandingPage launchApplication() throws IOException{
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
