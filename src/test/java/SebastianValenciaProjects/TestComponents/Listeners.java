package SebastianValenciaProjects.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import SebastianValenciaProjects.Resources.ExtentReportTestNG;

public class Listeners extends BaseTest implements ITestListener{
 	ExtentReports extent = ExtentReportTestNG.getReporObject();
	private ExtentTest test;
	ThreadLocal <ExtentTest> extentTest = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);//unique thread ID->>TEST
		}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.PASS,"Test pasarelas");

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().fail(result.getThrowable());
		String filePath = null;

		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			filePath = getScreenShot(result.getMethod().getMethodName(), driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//test.addScreenCaptureFromPath(filePath,);
		test.addScreenCaptureFromPath(filePath,result.getMethod().getMethodName());


	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.SKIP,"Test brincado");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}

}
