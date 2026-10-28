package SebastianValenciaProjects.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SebastianValenciaProjects.AbstractComponents.AbstractComponent;

public class CheckOutPage extends AbstractComponent{
	WebDriver driver;
	Actions a;


	public CheckOutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.a = new Actions (driver);
		PageFactory.initElements(driver, this);
		}

	@FindBy(css="[placeholder='Select Country']")
	private WebElement countryToInput;

	@FindBy(xpath="//button[contains(@class,ta-item)][2]")
	private WebElement countryToselect;

	@FindBy(css=".btnn.action__submit")
	private WebElement submit;

	@FindBy(css=".hero-primary")
	private WebElement confirmMessage;

	private By results = By.cssSelector(".ta-results");


	public void selectCountry(String country) {
		  // Send keys directly to the element: Actions-based key delivery is
		  // unreliable in headless Chrome (CI) and doesn't trigger Angular's
		  // input handler, so the .ta-results dropdown never appears
		  countryToInput.sendKeys(country);
		  waitForElementToAppear(results);
		  jsClick(countryToselect);
	}

	public ConfirmationPage goToSubmitPage(){
		  jsClick(submit);
		  ConfirmationPage confirmationPage = new ConfirmationPage(driver);
		  return confirmationPage;
	}





}
