package SebastianValenciaProjects.pageObjects;

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
		submit.click();
		ProductCatalogue productCatalogue = new ProductCatalogue(driver);
		return productCatalogue;
	}

	public String getErrorMessage(){
		waitForWebElementToAppear(errorMessage);
		return errorMessage.getText();
	}

	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client/#/auth/login");
	}


}
