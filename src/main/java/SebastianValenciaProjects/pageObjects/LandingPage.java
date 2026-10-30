package SebastianValenciaProjects.pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SebastianValenciaProjects.AbstractComponents.AbstractComponent;

public class LandingPage extends AbstractComponent{
	WebDriver driver;


	public LandingPage(WebDriver driver ) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id="userEmail")
	WebElement userEmail;

	@FindBy(id="userPassword")
	WebElement userPassword;

	@FindBy(id="login")
	WebElement submit;

	@FindBy(css="[class*='flyInOut']")
	WebElement errorMessage;

	public ProductCatalogue loginApplication(String emailU, String passU) {
		userEmail.sendKeys(emailU);
		userPassword.sendKeys(passU);
		// Use JS click to avoid ElementClickInterceptedException in headless CI
		// (overlays or smaller viewport can block the native click on Linux runners)
		jsClick(submit);
		ProductCatalogue productCatalogue = new ProductCatalogue(driver);
		return productCatalogue;
	}

	/**
	 * Returns the "type" attribute of the password input.
	 * Used to verify the field is masked (type="password").
	 */
	public String getPasswordFieldType() {
		return userPassword.getAttribute("type");
	}

	public String getErrorMessage(){
		waitForWebElementToAppear(errorMessage);
		// Use JS textContent instead of getText() to get the full message
		// regardless of animation state or viewport clipping in headless CI
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return ((String) js.executeScript("return arguments[0].textContent;", errorMessage)).trim();
	}

	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client/#/auth/login");
	}


}
