package SebastianValenciaProjects.Resources;

import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportTestNG {

		static ExtentReports extent;
		@BeforeTest
		public static ExtentReports getReporObject(){
			String path = System.getProperty("user.dir")+"//reports//index.html";
			ExtentSparkReporter reporter =  new ExtentSparkReporter(path);
			reporter.config().setReportName("Reportes de Djanguito");
			reporter.config().setDocumentTitle("Tests de Tuquito");
			extent = new ExtentReports();
			extent.attachReporter(reporter);
			extent.setSystemInfo("Tester", "Django alias Tuco");
			return extent;
		}


}
